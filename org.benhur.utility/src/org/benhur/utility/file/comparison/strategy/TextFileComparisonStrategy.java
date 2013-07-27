package org.benhur.utility.file.comparison.strategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class TextFileComparisonStrategy implements IFileComparisonStrategy
{
  public boolean compareFile(File file1, File file2)
  {
    boolean result = true;

    // Compare physical size.
    if (file1.length() == file2.length())
    {
      InputStream inputstream1 = null;
      InputStream inputstream2 = null;
      try
      {
        inputstream1 = new FileInputStream(file1);
        inputstream2 = new FileInputStream(file2);

        result = compareFile(inputstream1, inputstream2);
      }
      catch (Exception e)
      {
        result = false;
      }
      finally
      {
        try
        {
          if (inputstream1 != null)
          {
            inputstream1.close();
          }
          if (inputstream2 != null)
          {
            inputstream2.close();
          }
        }
        catch (Exception e)
        {
          result = false;
        }
      }
    }
    else
    {
      result = false;
    }

    return result;
  }

  public boolean compareFile(InputStream inputStream1, InputStream inputStream2)
  {
    int BUFFER_SIZE = 1024;
    byte buffer1[] = new byte[BUFFER_SIZE];
    byte buffer2[] = new byte[BUFFER_SIZE];

    boolean result = true;
    if (inputStream1 != inputStream2)
    {
      if (inputStream1 != null && inputStream2 != null)
      {
        try
        {
          int read1 = -1;
          int read2 = -1;

          do
          {
            // Fill buffers
            int offset1 = 0;
            while (offset1 < BUFFER_SIZE && (read1 = inputStream1.read(buffer1, offset1, BUFFER_SIZE - offset1)) >= 0)
            {
              offset1 += read1;
            }
            int offset2 = 0;
            while (offset2 < BUFFER_SIZE && (read2 = inputStream2.read(buffer2, offset2, BUFFER_SIZE - offset2)) >= 0)
            {
              offset2 += read2;
            }
            // Check offsets
            if (offset1 == offset2)
            {
              // Fill tail of buffers if no consistent data
              if (offset1 != BUFFER_SIZE)
              {
                Arrays.fill(buffer1, offset1, BUFFER_SIZE, (byte) 0);
              }
              if (offset2 != BUFFER_SIZE)
              {
                Arrays.fill(buffer2, offset2, BUFFER_SIZE, (byte) 0);
              }
              if (!Arrays.equals(buffer1, buffer2))
              {
                result = false;
              }
            }
            else
            {
              result = false;
            }
          }
          while (read1 >= 0 && read2 >= 0 && result == true);

          // both at EOF
          if (read1 < 0 && read2 < 0)
          {}
          else
          {
            result = false;
          }
        }
        catch (Exception e)
        {
          result = false;
        }
      }
      else
      {
        result = false;
      }
    }
    return result;
  }
}
