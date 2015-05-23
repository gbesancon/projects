package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ITable
{
  String getId();

  String getName();

  void addColumn(IColumn column);

  List<IColumn> getColumns();
}
