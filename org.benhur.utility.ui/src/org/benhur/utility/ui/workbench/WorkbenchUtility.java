package org.benhur.utility.ui.workbench;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPartReference;

@SuppressWarnings("restriction")
public class WorkbenchUtility
{
  public static Composite getComposite(IWorkbenchPart part)
  {
    WorkbenchPartReference workbenchPartReference = (WorkbenchPartReference) part.getSite().getPage()
        .getActivePartReference();
    return (Composite) workbenchPartReference.getPane().getControl();
  }

}
