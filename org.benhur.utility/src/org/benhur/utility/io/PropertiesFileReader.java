package org.benhur.utility.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.xml.bind.PropertyException;

public class PropertiesFileReader
{
  protected static String getPropertyFromFile(String filePath, String propertyName)
  {
    String propertyValue = null;
    FileInputStream fileInputStream = null;
    Properties properties = new Properties();

    File file = new File(filePath);

    if (file.exists())
    {
      try
      {
        fileInputStream = new FileInputStream(file);
        properties.load(fileInputStream);
        propertyValue = properties.getProperty(propertyName);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
      finally
      {
        if (fileInputStream != null)
        {
          try
          {
            fileInputStream.close();
          }
          catch (IOException e)
          {
            e.printStackTrace();
          }
        }
      }
    }
    return propertyValue;
  }

  protected static String getProperty(final String filepath, final String propertyName)
  {
    String propertyValue = null;

    if (filepath != null)
    {
      File file = new File(filepath);

      // Initialisation des variables
      String initialDirectory = file.getParent() + "/";
      String initialFileName = file.getName();
      String fileExtension = "";
      String newFilePath = "";

      if (!initialDirectory.isEmpty() && !initialFileName.isEmpty())
      {
        int dotIndex = initialFileName.lastIndexOf(".");
        if (dotIndex > 0)
        {
          fileExtension = initialFileName.substring(dotIndex);
          initialFileName = initialFileName.substring(0, dotIndex);
        }
        String localeLanguage = "";
        String localeCountry = "";
        if (!Locale.getDefault().getLanguage().isEmpty())
        {
          localeLanguage = "_" + Locale.getDefault().getLanguage();
        }
        if (!Locale.getDefault().getCountry().isEmpty())
        {
          localeCountry = "_" + Locale.getDefault().getCountry();
        }
        newFilePath = initialDirectory + initialFileName + localeLanguage + localeCountry + fileExtension;
        propertyValue = getPropertyFromFile(newFilePath, propertyName);
        if (propertyValue == null)
        {
          newFilePath = initialDirectory + initialFileName + localeLanguage + fileExtension;
          propertyValue = getPropertyFromFile(newFilePath, propertyName);
        }
        if (propertyValue == null)
        {
          propertyValue = getPropertyFromFile(filepath, propertyName);
        }
      }
    }
    return propertyValue;
  }

  public static String getStringProperty(final String pathFile, final String keyProperty) throws PropertyException
  {
    String result;
    String stringProperty = getProperty(pathFile, keyProperty);
    if (stringProperty == null)
    {
      throw new PropertyException("La propriété " + keyProperty + " n'est pas présente dans le fichier de propriétés "
          + pathFile + " .");
    }
    else
    {
      result = stringProperty;
    }
    return result;
  }

  public static int getIntProperty(final String pathFile, final String property) throws PropertyException
  {
    int result;
    String stringProperty = getProperty(pathFile, property);
    if (stringProperty == null)
    {
      throw new PropertyException("La propriété " + property + " n'est pas présente dans le fichier de propriétés "
          + pathFile + " .");
    }
    else
    {
      try
      {
        result = Integer.parseInt(stringProperty);
      }
      catch (NumberFormatException e)
      {
        throw new PropertyException("La propriété " + property + " du fichier de propriétés " + pathFile
            + " ne correspond pas à un nombre entier.");
      }
    }
    return result;
  }

  public static boolean getBooleanProperty(final String pathFile, final String property) throws PropertyException
  {
    boolean result;
    String stringProperty = getProperty(pathFile, property);
    if (stringProperty == null)
    {
      result = true;
    }
    else
    {
      try
      {
        result = Boolean.parseBoolean(stringProperty);
      }
      catch (NumberFormatException e)
      {
        throw new PropertyException("La propriété " + property + " du fichier de propriétés " + pathFile
            + " ne correspond pas à un booléen.");
      }
    }
    return result;
  }

  public static double getDoubleProperty(final String pathFile, final String property) throws PropertyException
  {
    double result;
    String stringProperty = getProperty(pathFile, property);
    if (stringProperty == null)
    {
      throw new PropertyException("La propriété " + property + " n'est pas présente dans le fichier de propriétés "
          + pathFile + " .");
    }
    else
    {
      try
      {
        result = Double.parseDouble(stringProperty);
      }
      catch (NumberFormatException e)
      {
        throw new PropertyException("La propriété " + property + " du fichier de propriétés " + pathFile
            + " ne correspond pas à un double.");
      }
    }
    return result;
  }
}
