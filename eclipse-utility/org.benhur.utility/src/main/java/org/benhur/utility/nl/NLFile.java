// Copyright (C) 2017 GBesancon

package org.benhur.utility.nl;

import java.util.Locale;
import org.benhur.utility.file.FileUtility;

public class NLFile {
  protected final String folderPath;
  protected final String baseFileName;
  protected final String fileExtension;
  protected final String localeLanguage;
  protected final String localeCountry;

  public NLFile(String fileName) {
    folderPath = FileUtility.getFolder(fileName);
    baseFileName = FileUtility.getBaseFilename(fileName);
    fileExtension = FileUtility.getExtension(fileName);
    if (!Locale.getDefault().getLanguage().isEmpty()) {
      localeLanguage = "_" + Locale.getDefault().getLanguage();
    } else {
      localeLanguage = "";
    }
    if (!Locale.getDefault().getCountry().isEmpty()) {
      localeCountry = "_" + Locale.getDefault().getCountry();
    } else {
      localeCountry = "";
    }
  }

  public String getFilename() {
    return folderPath + baseFileName + fileExtension;
  }

  public String getLanguageFilename() {
    return folderPath + baseFileName + localeLanguage + fileExtension;
  }

  public String getCountryFilename() {
    return folderPath + baseFileName + localeLanguage + localeCountry + fileExtension;
  }
}
