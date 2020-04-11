// Copyright (C) 2017 GBesancon

package org.benhur.utility.pool;

public interface PoolObjectProvider<T> {
  T createInstance();

  void cleanInstance(T instance);
}
