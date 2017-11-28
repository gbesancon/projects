package org.benhur.utility.plugin;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

public abstract class APlugin extends org.eclipse.core.runtime.Plugin
{
  protected PluginLogger logger;
  protected String pluginId;

  public APlugin(String pluginId)
  {
    this.pluginId = pluginId;
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    logger = new PluginLogger(this, pluginId);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    logger = null;
    super.stop(context);
  }

  public Preferences getPreferences()
  {
    return ConfigurationScope.INSTANCE.getNode(pluginId);
  }

  public PluginLogger getLogger()
  {
    return logger;
  }
}
