// Copyright (C) 2017 GBesancon

package org.benhur.utility.nl;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.xml.bind.PropertyException;
import org.benhur.utility.file.FileUtility;
import org.benhur.utility.io.PropertiesFileReader;

public class NLUtility {
  protected static final String NL_PREFIX = "@";

  protected static String getTranslationBaseFilename(URL xmlFileUrl) {
    String result = null;
    URL url = getTranslationBaseFileUrl(xmlFileUrl);
    if (url != null) {
      result = url.getFile();
    }
    return result;
  }

  protected static URL getTranslationBaseFileUrl(URL xmlFileUrl) {
    URL result = null;
    try {
      String translationFilename = FileUtility.changeExtension(xmlFileUrl.getFile(), ".properties");
      File translationFile = new File(translationFilename);
      if (translationFile.exists()) {
        result = translationFile.toURI().toURL();
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static void translate(Object jaxbObject, URL xmlFileUrl) {
    URL translationFileUrl = getTranslationBaseFileUrl(xmlFileUrl);
    if (translationFileUrl != null) {
      NLUtility.translate(jaxbObject, jaxbObject.getClass().getPackage(), translationFileUrl);
    }
  }

  public static void translate(Object jaxbObject, Package thePackage, URL translationFile) {
    Field[] fields = jaxbObject.getClass().getDeclaredFields();

    try {
      for (Field field : fields) {
        if (field.getType().isPrimitive()) {; // It's primitive, we do nothing.
        } else if (field.getType().equals(String.class)) {
          field.setAccessible(true);

          String stringValue = (String) field.get(jaxbObject);

          if (stringValue != null && stringValue.startsWith(NL_PREFIX)) {
            String key = stringValue.substring(1);
            String stringTranslated =
                PropertiesFileReader.getStringProperty(translationFile.getFile(), key);
            field.set(jaxbObject, stringTranslated);
          }

        } else if (field.getType().getPackage().equals(thePackage)) {
          field.setAccessible(true);
          if (field.get(jaxbObject) != null) {
            translate(field.get(jaxbObject), thePackage, translationFile);
          }
        } else if (field.getType().equals(List.class)) {
          field.setAccessible(true);

          List<?> list = (List<?>) field.get(jaxbObject);
          if (list != null) {
            for (Object item : list) {
              translate(item, thePackage, translationFile);
            }
          }
        }
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (PropertyException e) {
      e.printStackTrace();
    }
  }
}
