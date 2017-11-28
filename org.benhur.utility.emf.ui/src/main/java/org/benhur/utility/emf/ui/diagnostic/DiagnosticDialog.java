package org.benhur.utility.emf.ui.diagnostic;

import org.eclipse.emf.common.ui.DiagnosticComposite;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DiagnosticDialog extends org.eclipse.emf.common.ui.dialogs.DiagnosticDialog
{
  public static int open(Shell parent, String dialogTitle, String message, Diagnostic diagnostic)
  {
    return open(parent, dialogTitle, message, diagnostic, Diagnostic.OK | Diagnostic.INFO | Diagnostic.WARNING
        | Diagnostic.ERROR);
  }

  public static int openProblem(Shell parent, String dialogTitle, String message, Diagnostic diagnostic)
  {
    return open(parent, dialogTitle, message, diagnostic, DiagnosticComposite.ERROR_WARNING_MASK);
  }

  public static int open(Shell parentShell, String title, String message, Diagnostic diagnostic, int displayMask)
  {
    DiagnosticDialog dialog = new DiagnosticDialog(parentShell, title, message, diagnostic, displayMask);
    return dialog.open();
  }
  
  public DiagnosticDialog(Shell parentShell, String dialogTitle, String message, Diagnostic diagnostic, int severityMask)
  {
    super(parentShell, dialogTitle, message, diagnostic, severityMask);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    // create OK, Cancel and Details buttons
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
    createDetailsButton(parent);
  }
}
