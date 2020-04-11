// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.dgml.configuration;

import javax.xml.bind.PropertyException;
import org.benhur.utility.io.PropertiesFileReader;

public class Configuration
    extends org.benhur.utility.visualstudio.projectdependencies.configuration.Configuration {
  public Configuration(String configurationFile) {
    super(configurationFile);
  }

  public boolean getUseNameAsId() throws PropertyException {
    return PropertiesFileReader.getBooleanProperty(configurationFile, "USE_NAME_AS_ID");
  }
}
