package org.benhur.utility.ui.application.shell;

import org.benhur.utility.ui.application.AGraphicalApplication;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public abstract class AShellApplication extends AGraphicalApplication
{
  public AShellApplication(String applicationName)
  {
    super(applicationName);
  }

  @Override
  protected Object customStart(Display display)
  {
    Shell shell = new Shell(display);
    createShellContent(shell);
    return IApplication.EXIT_OK;
  }

  protected abstract void createShellContent(Shell shell);
}
