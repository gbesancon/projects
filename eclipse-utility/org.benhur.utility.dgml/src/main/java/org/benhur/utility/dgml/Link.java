// Copyright (C) 2017 GBesancon

package org.benhur.utility.dgml;

public class Link {
  public final Node source;
  public final Node target;
  public final Category category;

  public Link(Node source, Node target, Category category) {
    this.source = source;
    this.target = target;
    this.category = category;
  }
}
