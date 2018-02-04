// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.configuration;

import javax.xml.bind.PropertyException;

import org.benhur.utility.io.PropertiesFileReader;

public class Configuration
{
  protected String configurationFile;

  public Configuration(String configurationFile)
  {
    this.configurationFile = configurationFile;
  }

  public String getHost() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "HOST");
  }

  public int getPort() throws PropertyException
  {
    return PropertiesFileReader.getIntProperty(configurationFile, "PORT");
  }

  public String getUsername() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "USERNAME");
  }

  public String getPassword() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "PASSWORD");
  }

  public String getDatabaseName() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "DATABASE_NAME");
  }

  public String getGroupName(int[] ids) throws PropertyException
  {
    String propertyName = "GROUP_";
    for (int i = 0; i < ids.length; i++)
    {
      propertyName += ids[i] + "_";
    }
    propertyName += "NAME";
    return PropertiesFileReader.getStringProperty(configurationFile, propertyName);
  }

  public String getGroupTableNameIncludePattern(int[] ids) throws PropertyException
  {
    String propertyName = "GROUP_";
    for (int i = 0; i < ids.length; i++)
    {
      propertyName += ids[i] + "_";
    }
    propertyName += "TABLE_NAME_INCLUDE_PATTERN";
    return PropertiesFileReader.getStringProperty(configurationFile, propertyName);
  }

  public String getGroupTableNameExcludePattern(int[] ids) throws PropertyException
  {
    String propertyName = "GROUP_";
    for (int i = 0; i < ids.length; i++)
    {
      propertyName += ids[i] + "_";
    }
    propertyName += "TABLE_NAME_EXCLUDE_PATTERN";
    return PropertiesFileReader.getStringProperty(configurationFile, propertyName);
  }
}
