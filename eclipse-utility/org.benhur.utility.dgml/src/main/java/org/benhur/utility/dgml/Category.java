// Copyright (C) 2017 GBesancon

package org.benhur.utility.dgml;

public class Category {
  protected final String id;
  protected final String label;
  protected final String description;
  protected final boolean canBeDataDriven;
  protected final boolean canLinkedNodesBeDataDriven;
  protected final String incomingActionLabel;
  protected final boolean isContainment;
  protected final String outgoingActionLabel;

  public Category(
      String id,
      String label,
      String description,
      boolean canBeDataDriven,
      boolean canLinkedNodesBeDataDriven,
      String incomingActionLabel,
      boolean isContainment,
      String outgoingActionLabel) {
    this.id = id;
    this.label = label;
    this.description = description;
    this.canBeDataDriven = canBeDataDriven;
    this.canLinkedNodesBeDataDriven = canLinkedNodesBeDataDriven;
    this.incomingActionLabel = incomingActionLabel;
    this.isContainment = isContainment;
    this.outgoingActionLabel = outgoingActionLabel;
  }
}
