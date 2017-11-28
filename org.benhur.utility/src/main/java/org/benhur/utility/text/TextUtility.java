// Copyright (C) 2017 GBesancon

package org.benhur.utility.text;

public class TextUtility {
  public static String PadText(String text, int paddedTextSize, char paddingCharacter) {
    StringBuilder builder = new StringBuilder();
    builder.append(text);
    for (int iPaddedTextSize = text.length(); iPaddedTextSize < paddedTextSize; iPaddedTextSize++) {
      builder.append(paddingCharacter);
    }
    return builder.toString();
  }
}
