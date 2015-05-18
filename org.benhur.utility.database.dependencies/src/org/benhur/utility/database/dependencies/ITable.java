package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ITable
{
  String getName();

  void addDependency(ITable dependency);

  List<ITable> getDependencies();
}
