package org.benhur.utility.file.filter;

import java.io.File;
import java.io.FileFilter;

public class SkipHiddenFileFilter implements FileFilter
{
  @Override
  public boolean accept(File pathname)
  {
    return !pathname.isHidden();
  }
}
