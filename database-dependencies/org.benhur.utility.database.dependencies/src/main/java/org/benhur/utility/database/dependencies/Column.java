// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Column implements IColumn {
  protected final ITable table;
  protected final String id;
  protected final String name;
  protected final Set<IColumn> referedColumns;

  public Column(ITable table, String id, String name) {
    this.table = table;
    this.id = id;
    this.name = name;
    this.referedColumns = new HashSet<>();
  }

  @Override
  public ITable getTable() {
    return table;
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
  public void refersTo(IColumn referedColumn) {
    this.referedColumns.add(referedColumn);
  }

  @Override
  public Collection<IColumn> getReferedColumns() {
    return referedColumns;
  }
}
