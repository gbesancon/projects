package org.benhur.utility.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ADialog extends Dialog
{
  protected final String name;
  protected final int nbColumns;
  protected final Preferences preferences;

  public ADialog(String name, int nbColumns, Preferences preferences)
  {
    this(new Shell(SWT.TITLE | SWT.BORDER | SWT.SHELL_TRIM | SWT.APPLICATION_MODAL), name, nbColumns, preferences);
  }

  public ADialog(Shell parentShell, String name, int nbColumns, Preferences preferences)
  {
    super(parentShell);
    setShellStyle(parentShell.getStyle() | SWT.RESIZE);

    this.name = name;
    this.nbColumns = nbColumns;
    this.preferences = preferences;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText(name);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite container = (Composite) super.createDialogArea(parent);
    container.setLayout(new FillLayout());

    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayout(new GridLayout(nbColumns, false));

    createSpecialDialogArea(composite);

    return composite;
  }

  protected void createSpecialDialogArea(Composite composite)
  {}

  @Override
  protected void okPressed()
  {
    try
    {
      preferences.flush();
    }
    catch (BackingStoreException e)
    {
      e.printStackTrace();
    }
    super.okPressed();
  }

  @Override
  protected void cancelPressed()
  {
    super.cancelPressed();
  }
}
