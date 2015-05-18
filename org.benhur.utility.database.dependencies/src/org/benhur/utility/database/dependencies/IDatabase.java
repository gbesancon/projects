package org.benhur.utility.database.dependencies;

import java.util.List;

public interface IDatabase
{
  String getName();

  void addTable(ITable table);

  List<ITable> getTables();
}
