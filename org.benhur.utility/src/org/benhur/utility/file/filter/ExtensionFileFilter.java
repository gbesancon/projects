package org.benhur.utility.file.filter;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter
{
  protected String extension;

  public ExtensionFileFilter(String extension)
  {
    this.extension = extension;
  }

  @Override
  public boolean accept(File pathname)
  {
    return pathname.getName().endsWith("." + extension);
  }
}
