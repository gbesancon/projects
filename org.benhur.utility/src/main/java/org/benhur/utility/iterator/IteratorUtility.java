package org.benhur.utility.iterator;

import java.util.Iterator;

public class IteratorUtility
{
  public static int countItems(Iterator<?> iterator)
  {
    int count = 0;
    while (iterator.hasNext())
    {
      iterator.next();
      count++;
    }
    return count;
  }
}
