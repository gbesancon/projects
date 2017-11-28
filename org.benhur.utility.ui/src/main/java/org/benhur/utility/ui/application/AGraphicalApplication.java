package org.benhur.utility.ui.application;

import org.benhur.utility.application.AApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public abstract class AGraphicalApplication extends AApplication
{
  public AGraphicalApplication(String applicationName)
  {
    super(applicationName);
  }

  @Override
  protected Object customStart()
  {
    Display display = PlatformUI.createDisplay();
    try
    {
      return customStart(display);
    }
    finally
    {
      display.dispose();
    }
  }

  protected abstract Object customStart(Display display);
}
