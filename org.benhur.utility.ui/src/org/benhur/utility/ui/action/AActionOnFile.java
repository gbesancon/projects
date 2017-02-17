package org.benhur.utility.ui.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.actions.ActionDelegate;

public abstract class AActionOnFile extends ActionDelegate implements IActionDelegate
{
  protected List<IFile> files = null;
  protected PrintWriter printWriter = null;

  @SuppressWarnings("unchecked")
  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    if (selection instanceof IStructuredSelection)
    {
      files = ((IStructuredSelection) selection).toList();
    }
  }

  protected void initLogFile(String logPath, String prefix)
  {
    try
    {
      if (printWriter != null)
      {
        printWriter.flush();
        printWriter.close();
      }
      printWriter = new PrintWriter(
          logPath + File.separator + "log_" + System.currentTimeMillis() + "_" + prefix + ".log");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void run(IAction action)
  {
    specificRun(action);
    if (printWriter != null)
    {
      printWriter.flush();
      printWriter.close();
    }
  }

  protected abstract void specificRun(IAction action);

  protected void logException(Throwable e)
  {
    if (printWriter != null)
    {
      e.printStackTrace(printWriter);
    }
  }
}
