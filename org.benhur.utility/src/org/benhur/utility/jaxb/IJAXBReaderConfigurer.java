package org.benhur.utility.jaxb;

import java.net.URL;

public interface IJAXBReaderConfigurer
{
  public String getJAXBPackage();

  public URL getXsdUrl();

  public ClassLoader getClassLoader();
}
