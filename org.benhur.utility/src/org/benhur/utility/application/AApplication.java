package org.benhur.utility.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public abstract class AApplication implements IApplication
{
  protected final String application;
  protected IApplicationContext context = null;

  public AApplication(String application)
  {
    this.application = application;
  }

  @Override
  public Object start(IApplicationContext context) throws Exception
  {
    this.context = context;
    customStart();
    return IApplication.EXIT_OK;
  }

  protected void customStart()
  {}

  @Override
  public void stop()
  {
    customStop();
  }

  protected void customStop()
  {}
}
