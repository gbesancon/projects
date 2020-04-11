// Copyright (C) 2017 GBesancon

package org.benhur.utility.jaxb;

import java.net.URL;

public interface JAXBReaderConfigurer {
  public String getJAXBPackage();

  public URL getXsdUrl();

  public ClassLoader getClassLoader();
}
