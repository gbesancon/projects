package org.benhur.utility.ui.font;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

public class FontUtility
{
  public static final int FONT_MAX_SIZE = 9;

  public static Font changeSize(Font font, int newSize)
  {
    FontData[] fontData = font.getFontData();
    for (int i = 0; i < fontData.length; ++i)
    {
      if (newSize != fontData[i].getHeight())
      {
        fontData[i].setHeight(newSize);
      }
    }

    return new Font(Display.getCurrent(), fontData);
  }

  public static Font reduceSize(Font font, int newSize)
  {
    FontData[] fontData = font.getFontData();
    for (int i = 0; i < fontData.length; ++i)
    {
      if (newSize < fontData[i].getHeight())
      {
        fontData[i].setHeight(newSize);
      }
    }

    return new Font(Display.getCurrent(), fontData);
  }
}
