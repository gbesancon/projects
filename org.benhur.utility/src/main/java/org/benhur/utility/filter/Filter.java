package org.benhur.utility.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Filter<T>
{
  public List<T> filter(List<T> objects, IFilterKeyProvider<T> filterKeyProvider, String include, String exclude)
      throws PatternSyntaxException
  {
    List<T> includedObjects = filter(objects, filterKeyProvider, include, true);
    List<T> filteredObjects = filter(includedObjects, filterKeyProvider, exclude, false);
    return filteredObjects;
  }

  protected List<T> filter(List<T> objects, IFilterKeyProvider<T> filterKeyProvider, String regex, boolean inclusive)
      throws PatternSyntaxException
  {
    final List<T> filteredObjects;
    if (regex != null)
    {
      Pattern pattern = Pattern.compile(regex);

      if (pattern != null)
      {
        filteredObjects = new ArrayList<T>();
        for (T object : objects)
        {
          String key = filterKeyProvider.getKey(object);

          Matcher matcher = pattern.matcher(key);
          if (matcher.matches() == inclusive)
          {
            filteredObjects.add(object);
          }
        }
      }
      else
      {
        filteredObjects = objects;
      }
    }
    else
    {
      filteredObjects = objects;
    }
    return filteredObjects;
  }
}
