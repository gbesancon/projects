package org.benhur.utility.visualstudio.projectdependencies;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.benhur.utility.file.FileUtility;

public class Solution implements ISolution
{
  protected final File file;
  protected final String name;
  protected final List<IProject> projects;

  public Solution(File file)
  {
    this.file = file;
    this.name = FileUtility.getBaseFilename(file.getName());
    this.projects = new ArrayList<>();
  }

  @Override
  public File getFile()
  {
    return file;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void addProject(IProject project)
  {
    projects.add(project);
  }

  @Override
  public List<IProject> getProjects()
  {
    return projects;
  }
}
