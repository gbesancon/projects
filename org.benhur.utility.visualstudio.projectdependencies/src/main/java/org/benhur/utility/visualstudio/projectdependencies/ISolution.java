// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

import java.io.File;
import java.util.List;

public interface ISolution
{
  File getFile();

  String getName();

  void addProject(IProject project);

  List<IProject> getProjects();
}
