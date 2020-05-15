// Copyright (C) 2017 GBesancon

package org.benhur.utility.environment;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class EnvironmentUtility {
  public static String OS_NAME = System.getProperty("os.name");
  public static String OS_VERSION = System.getProperty("os.version");
  public static String OS_ARCH = System.getProperty("os.arch");
  public static boolean IS_WINDOWS = OS_NAME.startsWith("Windows");
  public static boolean IS_WINDOWS_XP =
      IS_WINDOWS && OS_NAME.equalsIgnoreCase("Windows XP") && OS_VERSION.equalsIgnoreCase("5.1");
  public static boolean IS_WINDOWS_VISTA =
      IS_WINDOWS && OS_NAME.equalsIgnoreCase("Windows Vista") && OS_VERSION.equalsIgnoreCase("5.3");
  public static boolean IS_WINDOWS_SEVEN =
      IS_WINDOWS && OS_NAME.equalsIgnoreCase("Windows Seven") && OS_VERSION.equalsIgnoreCase("5.4");
  public static boolean IS_MAC = OS_NAME.startsWith("Mac");
  public static boolean IS_LINUX = OS_NAME.equalsIgnoreCase("Linux");
  public static String USER_HOME = System.getProperty("user.home");

  public static File getUserHomeFile() {
    return new File(USER_HOME);
  }

  public static URL getUserHomeURL() {
    URL result = null;
    try {
      result = getUserHomeFile().toURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return result;
  }
}
