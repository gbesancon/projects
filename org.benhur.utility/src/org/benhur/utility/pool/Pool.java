package org.benhur.utility.pool;

import java.util.ArrayList;
import java.util.List;

public class Pool<T>
{
  protected final int mCapacity;
  protected final List<T> mPool;
  protected final PoolObjectProvider<T> mProvider;

  public Pool(PoolObjectProvider<T> provider)
  {
    this(provider, Integer.MAX_VALUE);
  }

  public Pool(PoolObjectProvider<T> provider, int capacity)
  {
    mCapacity = capacity;
    mProvider = provider;
    mPool = new ArrayList<T>(capacity);
  }

  public T getOut()
  {
    T object = null;
    if (mPool.size() > 0)
    {
      object = mPool.remove(0);
      if (object == null)
      {
        object = mProvider.createInstance();
      }
    }
    return object;
  }

  public void getIn(T object)
  {
    if (object != null)
    {
      mProvider.cleanInstance(object);
      if (mPool.size() < mCapacity)
      {
        mPool.add(object);
      }
    }
  }
}
