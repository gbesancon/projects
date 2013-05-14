package org.benhur.utility.ui.workbench;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPartReference;

public class WorkbenchUtility
{
  public static Composite getComposite(IWorkbenchPart part)
  {
    Composite result = null;
    WorkbenchPartReference workbenchPartReference = (WorkbenchPartReference) part.getSite().getPage()
        .getActivePartReference();
    result = (Composite) workbenchPartReference.getPane().getControl();
    return result;
  }

}
