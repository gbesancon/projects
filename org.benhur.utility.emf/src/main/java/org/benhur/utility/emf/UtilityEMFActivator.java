package org.benhur.utility.emf;

import org.benhur.utility.plugin.APlugin;
import org.osgi.framework.BundleContext;

public class UtilityEMFActivator extends APlugin
{
  public static final String PLUGIN_ID = "org.benhur.utility.emf"; //$NON-NLS-1$
  private static UtilityEMFActivator plugin;

  public UtilityEMFActivator()
  {
    super(PLUGIN_ID);
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    plugin = this;
    super.start(context);
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    super.stop(context);
    plugin = null;
  }

  public static UtilityEMFActivator getDefault()
  {
    return plugin;
  }
}
