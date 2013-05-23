package org.benhur.utility.regex;

import java.util.HashMap;
import java.util.Map;

public class RegExUtility
{
  public static char[] SPECIAL_CHARACTERS = { '<', '(', '[', '{', '\\', '^', '-', '=', '$', '!', '|', '}', ']', ')',
      '?', '*', '+', '.', '>' };
  public static Map<Character, String> SPECIAL_CHARACTERS_REPLACEMENT = new HashMap<Character, String>();
  static
  {
    for (char character : SPECIAL_CHARACTERS)
    {
      SPECIAL_CHARACTERS_REPLACEMENT.put(character, "\\" + character);
    }
  }

  public static String[] splitRegEx(String string)
  {
    return string.split("\\|");
  }

  public static String stringToRegEx(String string)
  {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < string.length(); i++)
    {
      char character = string.charAt(i);
      String replacementCharacter = SPECIAL_CHARACTERS_REPLACEMENT.get(character);
      if (replacementCharacter != null)
      {
        stringBuilder.append(replacementCharacter);
      }
      else
      {
        stringBuilder.append(character);
      }
    }
    return stringBuilder.toString();
  }
}
