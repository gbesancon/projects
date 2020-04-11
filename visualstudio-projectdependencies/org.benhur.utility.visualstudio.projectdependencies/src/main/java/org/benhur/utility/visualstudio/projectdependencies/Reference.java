// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

public class Reference extends AProjectDependency {
  public Reference(ISolution solution, String name) {
    super(solution, name, name);
  }
}
