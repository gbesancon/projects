// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.configuration;

import javax.xml.bind.PropertyException;

import org.benhur.utility.io.PropertiesFileReader;

public class Configuration
{
  protected String configurationFile;

  public Configuration(String configurationFile)
  {
    this.configurationFile = configurationFile;
  }

  public String getSolutionFile() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "SOLUTION_FILE");
  }

  public String getProjectNameIncludePattern() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "PROJECT_NAME_INCLUDE_PATTERN");
  }

  public String getProjectNameExcludePattern() throws PropertyException
  {
    return PropertiesFileReader.getStringProperty(configurationFile, "PROJECT_NAME_EXCLUDE_PATTERN");
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

  public String getGroupProjectNameIncludePattern(int[] ids) throws PropertyException
  {
    String propertyName = "GROUP_";
    for (int i = 0; i < ids.length; i++)
    {
      propertyName += ids[i] + "_";
    }
    propertyName += "PROJECT_NAME_INCLUDE_PATTERN";
    return PropertiesFileReader.getStringProperty(configurationFile, propertyName);
  }

  public String getGroupProjectNameExcludePattern(int[] ids) throws PropertyException
  {
    String propertyName = "GROUP_";
    for (int i = 0; i < ids.length; i++)
    {
      propertyName += ids[i] + "_";
    }
    propertyName += "PROJECT_NAME_EXCLUDE_PATTERN";
    return PropertiesFileReader.getStringProperty(configurationFile, propertyName);
  }
}
