package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITable
{
  protected final IDatabase database;
  protected final String id;
  protected final String name;
  protected final List<IColumn> columns;

  public Table(IDatabase database, String id, String name)
  {
    this.database = database;
    this.id = id;
    this.name = name;
    this.columns = new ArrayList<>();
  }

  @Override
  public IDatabase getDatabasse()
  {
    return database;
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
  public void addColumn(IColumn column)
  {
    columns.add(column);
  }

  @Override
  public List<IColumn> getColumns()
  {
    return columns;
  }
}
