package org.benhur.utility.ui.copy;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class CopyUtility
{
  public static String getTextFromTableViewer(TableViewer tableViewer)
  {
    StringBuffer stringBuffer = new StringBuffer();
    Table table = tableViewer.getTable();

    int[] columnIndexes = table.getColumnOrder();

    // Columns title
    TableColumn[] tableColumns = table.getColumns();
    for (int iColumn = 0; iColumn < tableColumns.length; iColumn++)
    {
      TableColumn tableColumn = tableColumns[iColumn];
      stringBuffer.append(replaceEndLineCharacter(tableColumn.getText()));
      if (iColumn == columnIndexes.length - 1)
      {
        stringBuffer.append("\n");
      }
      else
      {
        stringBuffer.append("\t");
      }
    }

    // Filter table items depending of selection.
    TableItem[] tableItems = null;
    if (!tableViewer.getSelection().isEmpty())
    {
      tableItems = table.getSelection();
    }
    else
    {
      tableItems = table.getItems();
    }

    // Contents
    for (TableItem tableItem : tableItems)
    {
      for (int iColumn = 0; iColumn < columnIndexes.length; iColumn++)
      {
        stringBuffer.append(replaceEndLineCharacter(tableItem.getText(columnIndexes[iColumn])));
        if (iColumn == columnIndexes.length - 1)
        {
          stringBuffer.append("\n");
        }
        else
        {
          stringBuffer.append("\t");
        }
      }
    }

    return stringBuffer.toString();
  }

  protected static String replaceEndLineCharacter(String text)
  {
    String result = "";
    if (text != null)
    {
      result = (text.contains("\n")) ? text.replaceAll("\n", ",") : text;
    }
    return result;
  }

  protected static BufferedImage convertSwtImageToAwtBufferedImage(Image image)
  {
    BufferedImage bufferedImage = null;
    ColorModel colorModel = null;
    ImageData imageData = image.getImageData();
    PaletteData paletteData = imageData.palette;
    if (paletteData.isDirect)
    {
      colorModel = new DirectColorModel(imageData.depth, paletteData.redMask, paletteData.greenMask,
          paletteData.blueMask);
      bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(imageData.width,
                                                                                              imageData.height), false,
          null);
      WritableRaster raster = bufferedImage.getRaster();
      int[] pixelArray = new int[3];
      for (int y = 0; y < imageData.height; y++)
      {
        for (int x = 0; x < imageData.width; x++)
        {
          int pixel = imageData.getPixel(x, y);
          RGB rgb = paletteData.getRGB(pixel);
          pixelArray[0] = rgb.red;
          pixelArray[1] = rgb.green;
          pixelArray[2] = rgb.blue;
          raster.setPixels(x, y, 1, 1, pixelArray);
        }
      }
    }
    else
    {
      RGB[] rgbs = paletteData.getRGBs();
      byte[] red = new byte[rgbs.length];
      byte[] green = new byte[rgbs.length];
      byte[] blue = new byte[rgbs.length];
      for (int i = 0; i < rgbs.length; i++)
      {
        RGB rgb = rgbs[i];
        red[i] = (byte) rgb.red;
        green[i] = (byte) rgb.green;
        blue[i] = (byte) rgb.blue;
      }
      if (imageData.transparentPixel != -1)
      {
        colorModel = new IndexColorModel(imageData.depth, rgbs.length, red, green, blue, imageData.transparentPixel);
      }
      else
      {
        colorModel = new IndexColorModel(imageData.depth, rgbs.length, red, green, blue);
      }
      bufferedImage = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(imageData.width,
                                                                                              imageData.height), false,
          null);
      WritableRaster writableRaster = bufferedImage.getRaster();
      int[] pixelArray = new int[1];
      for (int y = 0; y < imageData.height; y++)
      {
        for (int x = 0; x < imageData.width; x++)
        {
          int pixel = imageData.getPixel(x, y);
          pixelArray[0] = pixel;
          writableRaster.setPixel(x, y, pixelArray);
        }
      }
    }

    return bufferedImage;
  }

  public static boolean saveSwtCompositeToFile(String filename, Composite composite)
  {
    boolean result = false;
    GC gc = new GC(composite);
    Image image = new Image(Display.getCurrent(), composite.getBounds());
    gc.copyArea(image, 0, 0);
    result = saveSwtImageToFile(filename, image, SWT.IMAGE_PNG);
    gc.dispose();
    return result;
  }

  protected static boolean saveSwtImageToFile(String filename, Image image, int format)
  {
    boolean result = false;

    ImageLoader imageLoader = new ImageLoader();
    imageLoader.data = new ImageData[] { image.getImageData() };
    try
    {
      imageLoader.save(filename, format);
      result = true;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      result = false;
    }

    return result;
  }

  protected static boolean saveAwtBufferedImageToFile(String filename, BufferedImage bufferedImage)
  {
    boolean result = false;

    try
    {
      result = ImageIO.write(bufferedImage, "jpg", new File(filename));
    }
    catch (IOException e)
    {
      e.printStackTrace();
      result = false;
    }

    return result;
  }
}
