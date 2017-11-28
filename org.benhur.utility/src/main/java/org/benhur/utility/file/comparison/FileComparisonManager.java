// Copyright (C) 2017 GBesancon

package org.benhur.utility.file.comparison;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.benhur.utility.file.comparison.strategy.FileComparisonStrategy;
import org.benhur.utility.file.comparison.strategy.TextFileComparisonStrategy;

public class FileComparisonManager {
  protected FileComparisonStrategy defaultFileComparisonStrategy = null;
  protected Map<FileFilter, FileComparisonStrategy> fileComparisonStrategies =
      new HashMap<FileFilter, FileComparisonStrategy>();
  protected FileFilter comparedFileFilter = null;

  public FileComparisonManager() {
    init();
  }

  public void init() {
    defaultFileComparisonStrategy = new TextFileComparisonStrategy();
  }

  public void setDefaultFileComparisonStrategy(
      FileComparisonStrategy defaultFileComparisonStrategy) {
    this.defaultFileComparisonStrategy = defaultFileComparisonStrategy;
  }

  public void setComparedFileFilter(FileFilter comparedFileFilter) {
    this.comparedFileFilter = comparedFileFilter;
  }

  public void addFileComparisonStrategy(
      FileFilter fileFilter, FileComparisonStrategy fileComparisonStrategy) {
    fileComparisonStrategies.put(fileFilter, fileComparisonStrategy);
  }

  public FileComparisonStrategy getFileComparisonStrategy(File file) {
    FileComparisonStrategy result = null;
    for (Entry<FileFilter, FileComparisonStrategy> entries : fileComparisonStrategies.entrySet()) {
      if (entries.getKey().accept(file)) {
        result = entries.getValue();
      }
    }
    if (result == null) {
      result = defaultFileComparisonStrategy;
    }
    return result;
  }

  public boolean compare(String path1, String path2) {
    boolean result = true;

    File file1 = new File(path1);
    File file2 = new File(path2);
    if (file1.exists() && file2.exists()) {
      result = compare(file1, file2);
    } else {
      result = false;
    }

    return result;
  }

  public boolean compare(File file1, File file2) {
    boolean result = true;

    if (file1.isFile() && file2.isFile()) {
      if (comparedFileFilter.accept(file1) && comparedFileFilter.accept(file2)) {
        result = getFileComparisonStrategy(file1).compareFile(file1, file2);
      }
    } else if (file1.isDirectory() && file2.isDirectory()) {
      result = compareFolder(file1, file2);
    } else {
      result = false;
    }

    return result;
  }

  public boolean compareFolder(File file1, File file2) {
    boolean result = true;

    FileFilter fileFilter =
        new FileFilter() {
          public boolean accept(File pathname) {
            boolean result = true;
            if (comparedFileFilter == null) {
              result = true;
            } else {
              result = comparedFileFilter.accept(pathname);
            }
            return result;
          }
        };

    File[] content1 = file1.listFiles(fileFilter);
    File[] content2 = file2.listFiles(fileFilter);

    // Same number of file in the folder.
    if (content1.length == content2.length) {
      // Sort files
      Arrays.sort(
          content1,
          new Comparator<File>() {
            public int compare(File o1, File o2) {
              return o1.compareTo(o2);
            }
          });
      Arrays.sort(
          content2,
          new Comparator<File>() {
            public int compare(File o1, File o2) {
              return o1.compareTo(o2);
            }
          });

      // Compare files one by one
      // File with same name should be at the same index
      for (int i = 0; i < content1.length && result == true; i++) {
        result &= compare(content1[i], content2[i]);
      }
    } else {
      result = false;
    }

    return result;
  }
}
