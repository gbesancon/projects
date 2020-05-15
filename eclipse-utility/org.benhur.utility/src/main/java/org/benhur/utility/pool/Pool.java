// Copyright (C) 2017 GBesancon

package org.benhur.utility.pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T> {
  protected final int capacity;
  protected final List<T> pool;
  protected final PoolObjectProvider<T> provider;

  public Pool(PoolObjectProvider<T> provider) {
    this(provider, Integer.MAX_VALUE);
  }

  public Pool(PoolObjectProvider<T> provider, int capacity) {
    this.capacity = capacity;
    this.provider = provider;
    this.pool = new ArrayList<T>(capacity);
  }

  public T pull() {
    T object = null;
    if (pool.size() > 0) {
      object = pool.remove(0);
      if (object == null) {
        object = provider.createInstance();
      }
    }
    return object;
  }

  public void push(T object) {
    if (object != null) {
      provider.cleanInstance(object);
      if (pool.size() < capacity) {
        pool.add(object);
      }
    }
  }
}
