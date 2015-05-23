package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Database implements IDatabase
{
  protected final String id;
  protected final String name;
  protected final List<ITable> tables;

  public Database(String id, String name)
  {
    this.id = id;
    this.name = name;
    this.tables = new ArrayList<>();
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
  public void addTable(ITable table)
  {
    tables.add(table);
  }

  @Override
  public List<ITable> getTables()
  {
    return tables;
  }
}
