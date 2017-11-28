package org.benhur.utility.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvFileReader
{
  protected File file = null;
  protected String header = null;
  protected String separator = null;
  protected FileReader fileReader = null;
  protected BufferedReader buffer = null;
  protected int filePosition = 0;
  protected List<String> columnTitles = null;

  public CsvFileReader(File file, String header, String separator) throws IOException
  {
    this.file = file;
    this.header = header;
    this.separator = separator;
    this.fileReader = new FileReader(this.file);
    this.buffer = new BufferedReader(fileReader);
  }

  public long getFileSize()
  {
    return file.length();
  }

  public int getFilePosition()
  {
    return filePosition;
  }

  public List<String> getColumnTitles()
  {
    return columnTitles;
  }
}
