package org.benhur.utility.nl;

import java.util.Locale;

import org.benhur.utility.file.FileUtility;

public class NLFile
{
  protected final String mDirectory;
  protected final String mBaseFilename;
  protected final String mFileExtension;
  protected final String mLocaleLanguage;
  protected final String mLocaleCountry;

  public NLFile(String filename)
  {
    mDirectory = FileUtility.getFolder(filename);
    mBaseFilename = FileUtility.getBaseFilename(filename);
    mFileExtension = FileUtility.getExtension(filename);
    if (!Locale.getDefault().getLanguage().isEmpty())
    {
      mLocaleLanguage = "_" + Locale.getDefault().getLanguage();
    }
    else
    {
      mLocaleLanguage = "";
    }
    if (!Locale.getDefault().getCountry().isEmpty())
    {
      mLocaleCountry = "_" + Locale.getDefault().getCountry();
    }
    else
    {
      mLocaleCountry = "";
    }
  }

  public String getFilename()
  {
    return mDirectory + mBaseFilename + mFileExtension;
  }

  public String getLanguageFilename()
  {
    return mDirectory + mBaseFilename + mLocaleLanguage + mFileExtension;
  }

  public String getCountryFilename()
  {
    return mDirectory + mBaseFilename + mLocaleLanguage + mLocaleCountry + mFileExtension;
  }
}
