// Copyright (C) 2017 GBesancon

package org.benhur.utility.encoding;

public class UnicodeUtility {
  protected static final int HEXA_RADIX = 16;

  public static String convertUnicodeStringToString(String unicodeString) {
    String result = null;
    if (unicodeString.startsWith("\\u")) {
      result =
          new String(
              Character.toChars(
                  Integer.parseInt(unicodeString.substring("\\u".length()), HEXA_RADIX)));
    } else {
      result = unicodeString;
    }
    return result;
  }
}
