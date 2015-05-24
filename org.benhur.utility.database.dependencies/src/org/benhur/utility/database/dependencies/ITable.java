package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ITable
{
  IDatabase getDatabasse();

  String getId();

  String getName();

  void addColumn(IColumn column);

  List<IColumn> getColumns();
}
