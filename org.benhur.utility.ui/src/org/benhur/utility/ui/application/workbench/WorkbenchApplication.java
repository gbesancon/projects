package org.benhur.utility.ui.application.workbench;

import org.benhur.utility.ui.application.AGraphicalApplication;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public abstract class WorkbenchApplication extends AGraphicalApplication
{
  public WorkbenchApplication(String applicationName)
  {
    super(applicationName);
  }

  @Override
  protected Object customStart(Display display)
  {
    int returnCode = PlatformUI.createAndRunWorkbench(display, createWorkbenchAdvisor());
    if (returnCode == PlatformUI.RETURN_RESTART)
    {
      return IApplication.EXIT_RESTART;
    }
    else
    {
      return IApplication.EXIT_OK;
    }
  }

  protected abstract WorkbenchAdvisor createWorkbenchAdvisor();

  @Override
  protected void customStop()
  {
    if (PlatformUI.isWorkbenchRunning())
    {
      final IWorkbench workbench = PlatformUI.getWorkbench();
      final Display display = workbench.getDisplay();
      display.syncExec(new Runnable()
      {
        public void run()
        {
          if (!display.isDisposed())
          {
            workbench.close();
          }
        }
      });
    }
  }
}
