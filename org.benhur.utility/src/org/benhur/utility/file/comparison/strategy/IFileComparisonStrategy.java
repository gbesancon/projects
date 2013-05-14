package org.benhur.utility.file.comparison.strategy;

import java.io.File;

public interface IFileComparisonStrategy
{
  boolean compareFile(File file1, File file2);
}
