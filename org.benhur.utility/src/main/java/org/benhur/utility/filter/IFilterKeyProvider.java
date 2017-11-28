package org.benhur.utility.filter;

public interface IFilterKeyProvider<T>
{
  String getKey(T object);
}
