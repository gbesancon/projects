package org.benhur.utility.ui.service;

import org.eclipse.ui.PlatformUI;

public class ServiceUtility
{
  public static <T> T getService(Class<T> service)
  {
    return (T) PlatformUI.getWorkbench().getService(service);
  }
}
