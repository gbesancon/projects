package org.benhur.utility.ui.plugin;

import org.benhur.utility.plugin.PluginLogger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

public abstract class AUIPlugin extends org.eclipse.ui.plugin.AbstractUIPlugin
{
  protected PluginLogger mLogger;
  protected String mPluginId;

  public AUIPlugin(String pluginId)
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

  public ImageDescriptor getImageDescriptor(String path)
  {
    return imageDescriptorFromPlugin(mPluginId, path);
  }
}
