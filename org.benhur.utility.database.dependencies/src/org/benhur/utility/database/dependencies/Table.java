package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Table implements ITable
{
  protected final String name;
  protected final List<ITable> dependencies;

  public Table(String name)
  {
    this.name = name;
    this.dependencies = new ArrayList<>();
  }

  @Override
  public String getId()
  {
    return name;
  }

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void addDependency(ITable dependency)
  {
    dependencies.add(dependency);
  }

  @Override
  public List<ITable> getDependencies()
  {
    return dependencies;
  }
}
