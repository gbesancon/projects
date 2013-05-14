package org.benhur.utility.file.filter;

import java.io.FileFilter;

public class FileComparisonFilterCatalog
{
  public static FileFilter SKIP_HIDDEN_FILE_FILTER = new SkipHiddenFileFilter();

  public static FileFilter EXTENSION_FILE_FILTER(String extension)
  {
    return new ExtensionFileFilter(extension);
  }
}
