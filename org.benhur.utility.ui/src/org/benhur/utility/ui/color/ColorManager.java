package org.benhur.utility.ui.color;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ColorManager
{
  public final static String COLOR_BLACK = "black";
  public final static String COLOR_BLUE = "blue";
  public final static String COLOR_CYAN = "cyan";
  public final static String COLOR_DARKGRAY = "dark gray";
  public final static String COLOR_GRAY = "gray";
  public final static String COLOR_GREEN = "green";
  public final static String COLOR_MAGENTA = "magenta";
  public final static String COLOR_RED = "red";
  public final static String COLOR_WHITE = "white";
  public final static String COLOR_YELLOW = "yellow";
  public final static int COLOR_TEXT_R_INDEX_MIN = 0;
  public final static int COLOR_TEXT_R_INDEX_MAX = 2;
  public final static int COLOR_TEXT_G_INDEX_MIN = 2;
  public final static int COLOR_TEXT_G_INDEX_MAX = 4;
  public final static int COLOR_TEXT_B_INDEX_MIN = 4;
  public final static int COLOR_TEXT_B_INDEX_MAX = 6;
  public final static int COLOR_TEXT_RGB_SIZE = 6;

  private final static int HEXA_BASE = 16;
  private final Map<String, Color> mSystemColors = new HashMap<String, Color>();
  private final Map<String, Color> mUserColors = new HashMap<String, Color>();

  public ColorManager()
  {
    initSystemColors();
  }

  public Color getColor(String color)
  {
    Color result = null;

    if (color != null)
    {
      if (result == null)
      {
        result = mSystemColors.get(color);
      }

      // Couleur RGB?
      if (result == null)
      {
        result = mUserColors.get(color);
      }

      // Pas encore en cache
      if (result == null)
      {
        result = getRGBColor(color);
        if (result != null)
        {
          mUserColors.put(color, result);
        }
      }
    }

    return result;
  }

  private Color getRGBColor(String color)
  {
    Color result = null;
    if (color.length() == COLOR_TEXT_RGB_SIZE)
    {
      try
      {
        int r = Integer.parseInt(color.substring(COLOR_TEXT_R_INDEX_MIN, COLOR_TEXT_R_INDEX_MAX), HEXA_BASE);
        int g = Integer.parseInt(color.substring(COLOR_TEXT_G_INDEX_MIN, COLOR_TEXT_G_INDEX_MAX), HEXA_BASE);
        int b = Integer.parseInt(color.substring(COLOR_TEXT_B_INDEX_MIN, COLOR_TEXT_B_INDEX_MAX), HEXA_BASE);
        // RGB?
        result = new Color(Display.getCurrent(), new RGB(r, g, b));
      }
      catch (NumberFormatException e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }

  private void initSystemColors()
  {
    mSystemColors.put(COLOR_BLACK, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
    mSystemColors.put(COLOR_BLUE, Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
    mSystemColors.put(COLOR_CYAN, Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
    mSystemColors.put(COLOR_DARKGRAY, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
    mSystemColors.put(COLOR_GRAY, Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
    mSystemColors.put(COLOR_GREEN, Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
    mSystemColors.put(COLOR_MAGENTA, Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));
    mSystemColors.put(COLOR_RED, Display.getCurrent().getSystemColor(SWT.COLOR_RED));
    mSystemColors.put(COLOR_WHITE, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    mSystemColors.put(COLOR_YELLOW, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
  }

  public void dispose()
  {
    Iterator<Color> colors = mUserColors.values().iterator();
    while (colors.hasNext())
    {
      colors.next().dispose();
    }
  }
}
