// Copyright (C) 2017 GBesancon

package org.benhur.utility.dsm;

public class Edge {
  protected final Node source;
  protected final Node target;

  public Edge(Node source, Node target) {
    this.source = source;
    this.target = target;
  }

  public Node getSource() {
    return source;
  }

  public Node getTarget() {
    return target;
  }
}
