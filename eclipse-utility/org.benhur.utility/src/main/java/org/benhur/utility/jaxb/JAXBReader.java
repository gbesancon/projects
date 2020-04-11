// Copyright (C) 2017 GBesancon

package org.benhur.utility.jaxb;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.benhur.utility.file.FileUtility;
import org.benhur.utility.nl.NLUtility;
import org.xml.sax.SAXException;

public class JAXBReader<T> {
  protected JAXBReaderConfigurer jaxbReaderConfigurer = null;

  public JAXBReader(final JAXBReaderConfigurer jaxbReaderConfigurer) {
    this.jaxbReaderConfigurer = jaxbReaderConfigurer;
  }

  @SuppressWarnings("unchecked")
  public T readFile(final File xmlFile) {
    T result = null;
    try {
      if (xmlFile == null) {
        throw new JAXBException("XML file undefined.");
      }
      JAXBContext jaxbContext =
          JAXBContext.newInstance(
              jaxbReaderConfigurer.getJAXBPackage(), jaxbReaderConfigurer.getClassLoader());
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      if (jaxbReaderConfigurer.getXsdUrl() != null) {
        validateXmlFile(unmarshaller);
      }
      JAXBElement<T> root = (JAXBElement<T>) unmarshaller.unmarshal(xmlFile);
      result = root.getValue();

      String translationFilename =
          FileUtility.changeExtension(xmlFile.getAbsolutePath(), ".properties");
      File translationFile = new File(translationFilename);
      if (translationFile.exists()) {
        URL translationFileUrl = translationFile.toURI().toURL();

        if (translationFileUrl != null) {
          NLUtility.translate(result, result.getClass().getPackage(), translationFileUrl);
        }
      }
    } catch (JAXBException e) {
      e.printStackTrace();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return result;
  }

  protected void validateXmlFile(final Unmarshaller unmarshaller) {
    try {
      final SchemaFactory schemaFactory =
          SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(jaxbReaderConfigurer.getXsdUrl());
      unmarshaller.setSchema(schema);
    } catch (SAXException e) {
      e.printStackTrace();
    }
  }
}
