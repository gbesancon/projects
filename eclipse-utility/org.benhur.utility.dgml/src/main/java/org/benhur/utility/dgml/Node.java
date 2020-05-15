// Copyright (C) 2017 GBesancon

package org.benhur.utility.dgml;

public class Node {
  public final String id;
  public final String name;
  public final boolean isGroup;
  public final boolean isExpanded;

  public Node(String id, String name, boolean isGroup, boolean isExpanded) {
    this.id = id;
    this.name = name;
    this.isGroup = isGroup;
    this.isExpanded = isExpanded;
  }
}
