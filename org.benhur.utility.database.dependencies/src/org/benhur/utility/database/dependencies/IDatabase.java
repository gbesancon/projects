package org.benhur.utility.database.dependencies;

import java.util.List;

public interface IDatabase
{
  String getId();

  String getName();

  void addTable(ITable table);

  List<ITable> getTables();
}
