package org.benhur.utility.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;

public class FileLogListener implements ILogListener
{
  protected final String folderPath;
  protected final int severityMask;
  protected PrintStream logStream = null;

  public FileLogListener(String folderPath, int severityMask)
  {
    this.folderPath = folderPath;
    this.severityMask = severityMask;
  }

  public void init()
  {
    File logFolder = new File(folderPath);
    if (!logFolder.exists())
    {
      logFolder.mkdirs();
    }
    File logFile = new File(logFolder.getAbsolutePath() + File.separator + "log-" + System.currentTimeMillis() + ".log");
    try
    {
      logFile.createNewFile();
      logStream = new PrintStream(logFile);
      System.setOut(logStream);
      System.setErr(logStream);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public void finish()
  {
    if (logStream != null)
    {
      logStream.close();
      logStream = null;
    }
  }

  public void logging(IStatus status, String plugin)
  {
    if (status.matches(severityMask))
    {
      logStream.append(plugin + " : " + status.getMessage());
    }
  }
}
