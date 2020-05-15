// Copyright (C) 2017 GBesancon

package org.benhur.utility.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import javax.xml.bind.PropertyException;

public class PropertiesFileReader {
  protected static String getPropertyFromFile(String filePath, String propertyName) {
    String propertyValue = null;
    FileInputStream fileInputStream = null;
    Properties properties = new Properties();

    File file = new File(filePath);

    if (file.exists()) {
      try {
        fileInputStream = new FileInputStream(file);
        properties.load(fileInputStream);
        propertyValue = properties.getProperty(propertyName);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        if (fileInputStream != null) {
          try {
            fileInputStream.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return propertyValue;
  }

  protected static String getProperty(final String filePath, final String propertyName) {
    String propertyValue = null;

    if (filePath != null) {
      File file = new File(filePath);

      // Initialize variables
      String initialDirectory = file.getParent() != null ? file.getParent() + "/" : "";
      String initialFileName = file.getName();
      String fileExtension = "";
      String newFilePath = "";

      if (!initialDirectory.isEmpty() && !initialFileName.isEmpty()) {
        int dotIndex = initialFileName.lastIndexOf(".");
        if (dotIndex > 0) {
          fileExtension = initialFileName.substring(dotIndex);
          initialFileName = initialFileName.substring(0, dotIndex);
        }
        String localeLanguage = "";
        String localeCountry = "";
        if (!Locale.getDefault().getLanguage().isEmpty()) {
          localeLanguage = "_" + Locale.getDefault().getLanguage();
        }
        if (!Locale.getDefault().getCountry().isEmpty()) {
          localeCountry = "_" + Locale.getDefault().getCountry();
        }
        newFilePath =
            initialDirectory + initialFileName + localeLanguage + localeCountry + fileExtension;
        propertyValue = getPropertyFromFile(newFilePath, propertyName);
        if (propertyValue == null) {
          newFilePath = initialDirectory + initialFileName + localeLanguage + fileExtension;
          propertyValue = getPropertyFromFile(newFilePath, propertyName);
        }
        if (propertyValue == null) {
          propertyValue = getPropertyFromFile(filePath, propertyName);
        }
      }
    }
    return propertyValue;
  }

  public static String getStringProperty(final String filePath, final String propertyName)
      throws PropertyException {
    String result;
    String propertyValue = getProperty(filePath, propertyName);
    if (propertyValue == null) {
      throw new PropertyException(
          "Property " + propertyName + " is not present in property file " + filePath + " .");
    } else {
      result = propertyValue;
    }
    return result;
  }

  public static int getIntProperty(final String filePath, final String propertyName)
      throws PropertyException {
    int result;
    String propertyValue = getProperty(filePath, propertyName);
    if (propertyValue == null) {
      throw new PropertyException(
          "Property " + propertyName + " is not present in property file " + filePath + " .");
    } else {
      try {
        result = Integer.parseInt(propertyValue);
      } catch (NumberFormatException e) {
        throw new PropertyException(
            "Property " + propertyName + " in property file " + filePath + " is not an integer.");
      }
    }
    return result;
  }

  public static boolean getBooleanProperty(final String filePath, final String propertyName)
      throws PropertyException {
    boolean result;
    String propertyValue = getProperty(filePath, propertyName);
    if (propertyValue == null) {
      throw new PropertyException(
          "Property " + propertyName + " is not present in property file " + filePath + " .");
    } else {
      try {
        result = Boolean.parseBoolean(propertyValue);
      } catch (NumberFormatException e) {
        throw new PropertyException(
            "Property " + propertyName + " in property file " + filePath + " is not a boolean.");
      }
    }
    return result;
  }

  public static double getDoubleProperty(final String filePath, final String propertyName)
      throws PropertyException {
    double result;
    String propertyValue = getProperty(filePath, propertyName);
    if (propertyValue == null) {
      throw new PropertyException(
          "Property " + propertyName + " is not present in property file " + filePath + " .");
    } else {
      try {
        result = Double.parseDouble(propertyValue);
      } catch (NumberFormatException e) {
        throw new PropertyException(
            "Property " + propertyName + " in property file " + filePath + " is not a double.");
      }
    }
    return result;
  }
}
