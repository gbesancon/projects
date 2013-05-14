package org.benhur.utility.ui.composite;

import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.Preferences;

public class CompositeUtility
{
  public static Composite createTextBlock(Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String textPreference, final Preferences preferences)
  {
    final Label empty = new Label(composite, SWT.NONE);
    final Label label = new Label(composite, SWT.NONE);
    final Text text = new Text(composite, SWT.BORDER);
    // Behaviour
    label.setText(labelText + " :");
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    text.setText(preferences.get(textPreference, ""));
    GridData fillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    fillGridData.horizontalSpan = nbColumns - 2;
    text.setLayoutData(fillGridData);
    text.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        String textString = text.getText();
        preferences.put(textPreference, textString);
      }
    });
    return composite;
  }

  public static Composite createFileBlock(final Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String filePreference, final Preferences preferences,
      final String defaultFolder, final String[] filters)
  {
    final Label empty = new Label(composite, SWT.NONE);
    final Label label = new Label(composite, SWT.NONE);
    final Text text = new Text(composite, SWT.BORDER);
    final Button pathButton = new Button(composite, SWT.PUSH);
    // Behaviour
    label.setText(labelText + " :");
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    text.setText(preferences.get(filePreference, ""));
    GridData textFillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    textFillGridData.horizontalSpan = nbColumns - 3;
    text.setLayoutData(textFillGridData);
    text.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        String textString = text.getText();
        preferences.put(filePreference, textString);
      }
    });
    pathButton.setText("...");
    pathButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        FileDialog dialog = new FileDialog(composite.getShell(), SWT.SINGLE);
        // File extension.
        if (filters != null)
        {
          dialog.setFilterExtensions(filters);
        }

        // Default value preference.
        String preferenceValue = preferences.get(filePreference, "");
        if (preferenceValue == null || preferenceValue.equalsIgnoreCase(""))
        {
          preferenceValue = defaultFolder;
        }
        dialog.setFilterPath(preferenceValue);

        // Open dialog box.
        String file = dialog.open();
        if (file != null)
        {
          text.setText(file);
          preferences.put(filePreference, file);
        }
      }
    });
    return composite;
  }

  public static Composite createFolderBlock(final Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String folderPreference, final Preferences preferences,
      final String defaultFolder)
  {
    final Label empty = new Label(composite, SWT.NONE);
    final Label label = new Label(composite, SWT.NONE);
    final Text text = new Text(composite, SWT.BORDER);
    final Button folderButton = new Button(composite, SWT.PUSH);
    // Behaviour
    label.setText(labelText + " :");
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    text.setText(preferences.get(folderPreference, ""));
    GridData textFillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    textFillGridData.horizontalSpan = nbColumns - 3;
    text.setLayoutData(textFillGridData);
    text.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        String textString = text.getText();
        preferences.put(folderPreference, textString);
      }
    });
    folderButton.setText("...");
    folderButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(composite.getShell(), SWT.SINGLE);

        // Default value preference.
        String preferenceValue = preferences.get(folderPreference, "");
        if (preferenceValue == null || preferenceValue.equalsIgnoreCase(""))
        {
          preferenceValue = defaultFolder;
        }
        dialog.setFilterPath(preferenceValue);
        String folder = dialog.open();
        if (folder != null)
        {
          text.setText(folder);
          preferences.put(folderPreference, folder);
        }
      }
    });
    return composite;
  }

  public static Composite createSelectableFolderBlock(final Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String selectedPreference, final String folderPreference,
      final Preferences preferences, final String defaultFolder)
  {
    final Button selectedButton = new Button(composite, SWT.CHECK);
    final Label label = new Label(composite, SWT.NONE);
    final Text text = new Text(composite, SWT.BORDER);
    final Button folderButton = new Button(composite, SWT.PUSH);
    // Behaviour
    selectedButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        boolean selected = selectedButton.getSelection();
        preferences.putBoolean(selectedPreference, selected);

        label.setEnabled(selected);
        text.setEnabled(selected);
        folderButton.setEnabled(selected);
      }
    });
    selectedButton.setSelection(preferences.getBoolean(selectedPreference, false));
    label.setText(labelText + " :");
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    label.setEnabled(preferences.getBoolean(selectedPreference, false));
    text.setText(preferences.get(folderPreference, ""));
    GridData textFillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    textFillGridData.horizontalSpan = nbColumns - 3;
    text.setLayoutData(textFillGridData);
    text.setEnabled(preferences.getBoolean(selectedPreference, false));
    text.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        String textString = text.getText();
        preferences.put(folderPreference, textString);
      }
    });
    folderButton.setText("...");
    folderButton.setEnabled(preferences.getBoolean(selectedPreference, false));
    folderButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(composite.getShell(), SWT.SINGLE);
        // Default value preference.
        String preferenceValue = preferences.get(folderPreference, "");
        if (preferenceValue == null || preferenceValue.equalsIgnoreCase(""))
        {
          preferenceValue = defaultFolder;
        }
        String folder = dialog.open();
        text.setText(folder);
        preferences.put(folderPreference, folder);
      }
    });
    return composite;
  }

  public static Composite createSelectableBlock(Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String selectedPreference, final Preferences preferences)
  {
    final Button selectedButton = new Button(composite, SWT.CHECK);
    final Label label = new Label(composite, SWT.NONE);
    // Behaviour
    selectedButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        boolean selected = selectedButton.getSelection();
        preferences.putBoolean(selectedPreference, selected);
      }
    });
    selectedButton.setSelection(preferences.getBoolean(selectedPreference, false));
    label.setText(labelText);
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    GridData fillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    fillGridData.horizontalSpan = nbColumns - 1;
    label.setLayoutData(fillGridData);
    return composite;
  }

  public static Group createSelectableGroupOption(Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String selectedPreference, final Preferences preferences)
  {
    final Button selectedButton = new Button(composite, SWT.CHECK);
    final Group group = new Group(composite, SWT.NONE);
    // Behaviour
    selectedButton.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        boolean selected = selectedButton.getSelection();
        group.setEnabled(selected);
        preferences.putBoolean(selectedPreference, selected);
      }
    });
    selectedButton.setSelection(preferences.getBoolean(selectedPreference, false));
    group.setText(labelText);
    group.setToolTipText(labelTooltipText);
    group.setEnabled(preferences.getBoolean(selectedPreference, false));
    GridData fillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    fillGridData.horizontalSpan = nbColumns - 1;
    group.setLayoutData(fillGridData);
    return group;
  }

  public static Group createGroupBlock(Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText)
  {
    final Group group = new Group(composite, SWT.NONE);
    group.setText(labelText);
    group.setToolTipText(labelTooltipText);
    GridData fillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    fillGridData.horizontalSpan = nbColumns;
    group.setLayoutData(fillGridData);
    return group;
  }

  public static Composite createScrolledComposite(Composite composite, int nbColumns)
  {
    final ScrolledComposite scrolledComposite = new ScrolledComposite(composite, SWT.V_SCROLL);
    GridData scrolledCompositeGridData = new GridData(GridData.FILL_BOTH);
    scrolledCompositeGridData.horizontalSpan = nbColumns;
    scrolledComposite.setLayoutData(scrolledCompositeGridData);
    GridLayout gridLayout = new GridLayout(nbColumns, false);
    scrolledComposite.setLayout(gridLayout);

    final Composite parent = new Composite(scrolledComposite, SWT.NONE);
    parent.setLayoutData(scrolledCompositeGridData);
    parent.setLayout(gridLayout);

    scrolledComposite.setContent(parent);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.addControlListener(new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        Rectangle r = scrolledComposite.getClientArea();
        scrolledComposite.setMinSize(parent.computeSize(r.width, SWT.DEFAULT));
      }
    });
    return parent;
  }

  public static Composite createDateTimeBlock(Composite composite, int nbColumns, final String labelText,
      final String labelTooltipText, final String dateTimePreference, final Preferences preferences)
  {
    final Label empty = new Label(composite, SWT.NONE);
    final Label label = new Label(composite, SWT.NONE);
    final CDateTime dateTime = new CDateTime(composite, CDT.BORDER | CDT.TAB_FIELDS | CDT.DROP_DOWN | CDT.DATE_LONG
        | CDT.TIME_MEDIUM);
    // Behaviour
    label.setText(labelText + " :");
    if (labelTooltipText != null)
    {
      label.setToolTipText(labelTooltipText);
    }
    dateTime.setSelection(new Date(preferences.getLong(dateTimePreference, System.currentTimeMillis())));
    GridData textFillGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    textFillGridData.horizontalSpan = nbColumns - 2;
    dateTime.setLayoutData(textFillGridData);
    dateTime.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        if (e.data != null)
        {
          long time = ((Date) (e.data)).getTime();
          preferences.putLong(dateTimePreference, time);
        }
        else
        {
          preferences.remove(dateTimePreference);
        }
      }
    });
    return composite;
  }
}
