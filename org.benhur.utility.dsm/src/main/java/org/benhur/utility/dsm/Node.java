// Copyright (C) 2017 GBesancon

package org.benhur.utility.dsm;

public class Node {
  protected String id;
  protected String name;

  public Node(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
