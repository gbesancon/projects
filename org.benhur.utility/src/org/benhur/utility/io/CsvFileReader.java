package org.benhur.utility.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvFileReader
{
  protected File mFile = null;
  protected FileReader mFileReader = null;
  protected BufferedReader mBuffer = null;
  protected String mHeader = null;
  protected String mSeparator = null;

  protected List<String> mColumnTitles = null;

  protected int mFilePosition = 0;

  public CsvFileReader(File file, String header, String separator) throws IOException
  {
    mFile = file;
    mHeader = header;
    mSeparator = separator;
    mFileReader = new FileReader(mFile);
    mBuffer = new BufferedReader(mFileReader);
  }

  public long getFileSize()
  {
    return mFile.length();
  }

  public int getFilePosition()
  {
    return mFilePosition;
  }

  public List<String> getColumnTitles()
  {
    return mColumnTitles;
  }
}
