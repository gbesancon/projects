// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

import java.io.File;
import java.util.List;

public class ProjectReference implements IProjectDependency, IProject {
  protected final IProject project;

  public ProjectReference(IProject project) {
    this.project = project;
  }

  @Override
  public String getId() {
    return project.getId();
  }

  @Override
  public ISolution getSolution() {
    return project.getSolution();
  }

  @Override
  public String getName() {
    return project.getName();
  }

  @Override
  public File getFile() {
    return project.getFile();
  }

  @Override
  public void addProjectDependency(IProjectDependency projectDependency) {
    project.addProjectDependency(projectDependency);
  }

  @Override
  public List<IProjectDependency> getProjectDependencies() {
    return project.getProjectDependencies();
  }
}
