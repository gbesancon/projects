// Copyright (C) 2017 GBesancon

package org.benhur.utility.filter;

public interface FilterableKeyProvider<T> {
  String getKey(T object);
}
