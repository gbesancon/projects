package org.benhur.utility.ui.application;

import org.benhur.utility.application.AApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public abstract class AGraphicalApplication extends AApplication
{
  public AGraphicalApplication(String application)
  {
    super(application);
  }

  @Override
  protected void customStart()
  {
    Display display = PlatformUI.createDisplay();
    try
    {
      Shell shell = new Shell(display);
      createShellContent(shell);
    }
    finally
    {
      display.dispose();
    }
  }

  protected abstract void createShellContent(Shell shell);
}
