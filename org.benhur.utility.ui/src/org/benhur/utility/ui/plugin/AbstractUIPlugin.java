package org.benhur.utility.ui.plugin;

import org.benhur.utility.plugin.PluginLogger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

public abstract class AbstractUIPlugin extends org.eclipse.ui.plugin.AbstractUIPlugin
{
  protected PluginLogger mLogger;
  protected String mPluginId;

  public AbstractUIPlugin(String pluginId)
  {
    mPluginId = pluginId;
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    mLogger = new PluginLogger(this, mPluginId);
    super.start(context);
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
