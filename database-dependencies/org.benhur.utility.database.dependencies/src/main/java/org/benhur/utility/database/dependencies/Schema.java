// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Schema implements ISchema {
  protected final ICatalog catalog;
  protected final String id;
  protected final String name;
  protected final List<ITable> tables;

  public Schema(ICatalog catalog, String id, String name) {
    this.catalog = catalog;
    this.id = id;
    this.name = name;
    this.tables = new ArrayList<>();
  }

  @Override
  public ICatalog getCatalog() {
    return catalog;
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
  public void addTable(ITable table) {
    tables.add(table);
  }

  @Override
  public List<ITable> getTables() {
    return tables;
  }
}
