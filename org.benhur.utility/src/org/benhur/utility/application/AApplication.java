package org.benhur.utility.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public abstract class AApplication implements IApplication
{
  protected final String applicationName;
  protected IApplicationContext context = null;

  public AApplication(String applicationName)
  {
    this.applicationName = applicationName;
  }

  public Object start(IApplicationContext context) throws Exception
  {
    this.context = context;
    return customStart();
  }

  protected Object customStart()
  {
    return IApplication.EXIT_OK;
  }

  public void stop()
  {
    customStop();
  }

  protected void customStop()
  {}
}
