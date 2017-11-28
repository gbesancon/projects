package org.benhur.utility.ui.color;

import java.util.Collection;
import java.util.HashMap;
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
  private final Map<String, Color> systemColors = new HashMap<String, Color>();
  private final Map<String, Color> userColors = new HashMap<String, Color>();

  public ColorManager()
  {
    initSystemColors();
  }

  public Color getColor(String color)
  {
    Color result = null;
    if (color != null)
    {
      // System color by name
      result = systemColors.get(color);
      if (result == null)
      {
        // User color by RGB from cache
        result = userColors.get(color);
        if (result == null)
        {
          // User color by RGB not in cache
          result = getRGBColor(color);
          if (result != null)
          {
            userColors.put(color, result);
          }
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
    systemColors.put(COLOR_BLACK, Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
    systemColors.put(COLOR_BLUE, Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
    systemColors.put(COLOR_CYAN, Display.getCurrent().getSystemColor(SWT.COLOR_CYAN));
    systemColors.put(COLOR_DARKGRAY, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
    systemColors.put(COLOR_GRAY, Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
    systemColors.put(COLOR_GREEN, Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
    systemColors.put(COLOR_MAGENTA, Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));
    systemColors.put(COLOR_RED, Display.getCurrent().getSystemColor(SWT.COLOR_RED));
    systemColors.put(COLOR_WHITE, Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
    systemColors.put(COLOR_YELLOW, Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW));
  }

  public void dispose()
  {
    Collection<Color> colors = userColors.values();
    for (Color color : colors)
    {
      color.dispose();
    }
  }
}
