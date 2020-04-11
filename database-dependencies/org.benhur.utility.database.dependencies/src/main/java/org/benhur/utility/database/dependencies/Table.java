// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITable {
  protected final ISchema schema;
  protected final String id;
  protected final String name;
  protected final String type;
  protected final String remarks;
  protected final List<IColumn> columns;

  public Table(ISchema schema, String id, String name, String type, String remarks) {
    this.schema = schema;
    this.id = id;
    this.name = name;
    this.type = type;
    this.remarks = remarks;
    this.columns = new ArrayList<>();
  }

  @Override
  public ISchema getSchema() {
    return schema;
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
  public String getType() {
    return type;
  }

  @Override
  public String getRemarks() {
    return remarks;
  }

  @Override
  public void addColumn(IColumn column) {
    columns.add(column);
  }

  @Override
  public List<IColumn> getColumns() {
    return columns;
  }
}
