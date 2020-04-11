// Copyright (C) 2017 GBesancon

package org.benhur.utility.file.comparison.strategy;

import java.io.File;

public interface FileComparisonStrategy {
  boolean compareFile(File file1, File file2);
}
