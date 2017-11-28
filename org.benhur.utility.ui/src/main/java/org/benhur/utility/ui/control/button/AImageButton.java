package org.benhur.utility.ui.control.button;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public abstract class AImageButton<T> extends WorkbenchWindowControlContribution
{
  protected Button button = null;
  protected Map<T, Image> images = new HashMap<T, Image>();

  public AImageButton()
  {
    initImages(images);
  }

  public AImageButton(String id)
  {
    super(id);
    initImages(images);
  }

  protected abstract void initImages(Map<T, Image> images);

  @Override
  protected Control createControl(Composite parent)
  {
    parent.setLayout(new FillLayout());
    button = new Button(parent, SWT.BACKGROUND);
    button.addSelectionListener(new SelectionListener()
    {
      public void widgetSelected(SelectionEvent e)
      {
        widgetDefaultSelected(e);
      }

      public void widgetDefaultSelected(SelectionEvent e)
      {
        T key = actionOnSelection();
        setImage(key);
      }
    });
    return button;
  }

  protected abstract T actionOnSelection();

  protected void setImage(T key)
  {
    Image image = images.get(key);
    button.setBackgroundImage(image);
  }
}
