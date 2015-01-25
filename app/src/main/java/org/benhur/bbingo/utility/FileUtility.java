package org.benhur.bbingo.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class FileUtility
{
  public static void CopyFile(File src, File dest)
  {
    try
    {
      InputStream in = new FileInputStream(src);
      OutputStream out = new FileOutputStream(dest);

      Copyfile(in, out);
    }
    catch (FileNotFoundException e)
    {
      Log.e(FileUtility.class.getName(), e.getMessage(), e);
    }
  }

  public static void Copyfile(InputStream in, OutputStream out)
  {
    try
    {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = in.read(buffer)) > 0)
      {
        out.write(buffer, 0, length);
      }
      in.close();
      out.close();
    }
    catch (IOException e)
    {
      Log.e(FileUtility.class.getName(), e.getMessage(), e);
    }
  }
}
