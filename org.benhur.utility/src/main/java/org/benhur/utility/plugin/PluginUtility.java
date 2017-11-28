package org.benhur.utility.plugin;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.Version;

public class PluginUtility
{
  public static String getVersion(Plugin plugin)
  {
    String versionString = "X.Y.Z.qualifier";
    if (plugin != null)
    {
      Bundle bundle = plugin.getBundle();
      if (bundle != null)
      {
        Version version = bundle.getVersion();
        if (version != null)
        {
          versionString = version.toString();
        }
      }
    }
    return versionString;
  }
}
