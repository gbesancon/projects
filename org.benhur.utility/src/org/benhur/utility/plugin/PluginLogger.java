package org.benhur.utility.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

public class PluginLogger
{
  protected final Plugin mPlugin;
  protected final String mPluginId;

  public PluginLogger(Plugin plugin, String pluginId)
  {
    mPlugin = plugin;
    mPluginId = pluginId;
  }

  public void log(IStatus status)
  {
    mPlugin.getLog().log(status);
  }

  public void error(String message, Throwable throwable)
  {
    log(new Status(Status.ERROR, mPluginId, message, throwable));
  }

  public void error(Throwable throwable)
  {
    log(new Status(Status.ERROR, mPluginId, throwable.getMessage(), throwable));
  }

  public void error(String message)
  {
    log(new Status(Status.ERROR, mPluginId, message));
  }

  public void warning(String message, Throwable throwable)
  {
    log(new Status(Status.WARNING, mPluginId, message, throwable));
  }

  public void warning(Throwable throwable)
  {
    log(new Status(Status.WARNING, mPluginId, throwable.getMessage(), throwable));
  }

  public void warning(String message)
  {
    log(new Status(Status.WARNING, mPluginId, message));
  }

  public void info(String message, Throwable throwable)
  {
    log(new Status(Status.INFO, mPluginId, message, throwable));
  }

  public void info(Throwable throwable)
  {
    log(new Status(Status.INFO, mPluginId, throwable.getMessage(), throwable));
  }

  public void info(String message)
  {
    log(new Status(Status.INFO, mPluginId, message));
  }
}
