package org.benhur.utility.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  public static boolean checkValue(String value, String valueIncludePatternString, String valueExcludePatternString)
  {
    boolean acceptValue = false;
    boolean valueInclude = true; // By default accept all project names
    if (!valueIncludePatternString.equalsIgnoreCase(""))
    {
      Pattern valueIncludePattern = Pattern.compile(valueIncludePatternString);
      Matcher valueIncludeMatcher = valueIncludePattern.matcher(value);
      valueInclude = valueIncludeMatcher.find();
    }

    boolean valueExclude = false; // By default we exclude none of the project names
    if (!valueExcludePatternString.equalsIgnoreCase(""))
    {
      Pattern valueExcludePattern = Pattern.compile(valueExcludePatternString);
      Matcher valueExcludeMatcher = valueExcludePattern.matcher(value);
      valueExclude = valueExcludeMatcher.find();
    }

    if (valueInclude && !valueExclude)
    {
      acceptValue = true;
    }
    return acceptValue;
  }
}
