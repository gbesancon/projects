package org.benhur.utility.plugin;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

public abstract class APlugin extends org.eclipse.core.runtime.Plugin
{
  protected PluginLogger mLogger;
  protected String mPluginId;

  public APlugin(String pluginId)
  {
    mPluginId = pluginId;
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    mLogger = new PluginLogger(this, mPluginId);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    mLogger = null;
    super.stop(context);
  }

  public Preferences getPreferences()
  {
    return ConfigurationScope.INSTANCE.getNode(mPluginId);
  }

  public PluginLogger getLogger()
  {
    return mLogger;
  }
}
