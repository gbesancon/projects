// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

public class Column implements IColumn
{
  protected final ITable table;
  protected final String id;
  protected final String name;
  protected IColumn foreignColumn;

  public Column(ITable table, String id, String name)
  {
    this.table = table;
    this.id = id;
    this.name = name;
    this.foreignColumn = null;
  }

  @Override
  public ITable getTable()
  {
    return table;
  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setForeignColumn(IColumn foreignColumn)
  {
    this.foreignColumn = foreignColumn;
  }

  @Override
  public IColumn getForeignColumn()
  {
    return foreignColumn;
  }
}
