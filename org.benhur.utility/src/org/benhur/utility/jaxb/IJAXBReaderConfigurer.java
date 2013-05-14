package org.benhur.utility.jaxb;

import java.net.URL;

public interface IJAXBReaderConfigurer
{
  public String getJaxbPackage();

  public URL getXsdUrl();

  public ClassLoader getClassLoader();
}
