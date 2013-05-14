package org.benhur.utility.workspace;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class WorkspaceUtility
{
  public static IWorkspace getWorkspace()
  {
    return ResourcesPlugin.getWorkspace();
  }

  public static String getWorkspaceFolder()
  {
    return getWorkspace().getRoot().getLocation().toString();
  }

  public static IProject createProject(String name, IProgressMonitor monitor)
  {
    IProject project = null;
    try
    {
      IWorkspaceRoot root = getWorkspace().getRoot();
      project = root.getProject(name);
      project.create(monitor);
      project.open(monitor);
      IProjectDescription description = project.getDescription();
      project.setDescription(description, monitor);
    }
    catch (CoreException e)
    {
      e.printStackTrace();
    }
    return project;
  }

  public static void refreshProject(IProject project, IProgressMonitor monitor)
  {
    try
    {
      project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
    }
    catch (CoreException e)
    {
      e.printStackTrace();
    }
  }
}
