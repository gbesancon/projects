package org.benhur.utility.ui.service;

import org.eclipse.ui.PlatformUI;

public class ServiceUtility
{
  @SuppressWarnings("unchecked")
  public static <T> T getService(Class<T> serviceClass)
  {
    return (T) PlatformUI.getWorkbench().getService(serviceClass);
  }
}
