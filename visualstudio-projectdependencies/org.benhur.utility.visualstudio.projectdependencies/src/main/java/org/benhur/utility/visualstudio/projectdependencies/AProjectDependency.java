// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

import java.util.ArrayList;
import java.util.List;

public abstract class AProjectDependency implements IProjectDependency {
  protected final ISolution solution;
  protected final String id;
  protected final String name;
  protected final List<IProjectDependency> projectDependencies;

  public AProjectDependency(ISolution solution, String id, String name) {
    this.solution = solution;
    this.id = id;
    this.name = name;
    this.projectDependencies = new ArrayList<>();
  }

  @Override
  public ISolution getSolution() {
    return solution;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addProjectDependency(IProjectDependency projectDependency) {
    projectDependencies.add(projectDependency);
  }

  @Override
  public List<IProjectDependency> getProjectDependencies() {
    return projectDependencies;
  }
}
