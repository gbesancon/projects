package org.benhur.utility.ui.plugin;

import org.benhur.utility.plugin.PluginLogger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

public abstract class AUIPlugin extends org.eclipse.ui.plugin.AbstractUIPlugin
{
  protected PluginLogger logger;
  protected String pluginId;

  public AUIPlugin(String pluginId)
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

  public ImageDescriptor getImageDescriptor(String path)
  {
    return imageDescriptorFromPlugin(pluginId, path);
  }
}
