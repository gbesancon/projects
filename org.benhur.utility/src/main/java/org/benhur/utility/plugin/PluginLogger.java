package org.benhur.utility.plugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

public class PluginLogger
{
  protected final Plugin plugin;
  protected final String pluginId;

  public PluginLogger(Plugin plugin, String pluginId)
  {
    this.plugin = plugin;
    this.pluginId = pluginId;
  }

  public void log(IStatus status)
  {
    plugin.getLog().log(status);
  }

  public void error(String message, Throwable throwable)
  {
    log(new Status(Status.ERROR, pluginId, message, throwable));
  }

  public void error(Throwable throwable)
  {
    log(new Status(Status.ERROR, pluginId, throwable.getMessage(), throwable));
  }

  public void error(String message)
  {
    log(new Status(Status.ERROR, pluginId, message));
  }

  public void warning(String message, Throwable throwable)
  {
    log(new Status(Status.WARNING, pluginId, message, throwable));
  }

  public void warning(Throwable throwable)
  {
    log(new Status(Status.WARNING, pluginId, throwable.getMessage(), throwable));
  }

  public void warning(String message)
  {
    log(new Status(Status.WARNING, pluginId, message));
  }

  public void info(String message, Throwable throwable)
  {
    log(new Status(Status.INFO, pluginId, message, throwable));
  }

  public void info(Throwable throwable)
  {
    log(new Status(Status.INFO, pluginId, throwable.getMessage(), throwable));
  }

  public void info(String message)
  {
    log(new Status(Status.INFO, pluginId, message));
  }
}
