// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

import java.util.List;

public interface ISolutionItem {
  ISolution getSolution();

  String getId();

  String getName();

  void addProjectDependency(IProjectDependency projectDependency);

  List<IProjectDependency> getProjectDependencies();
}
