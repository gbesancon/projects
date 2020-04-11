// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ITable {
  ISchema getSchema();

  String getId();

  String getName();

  String getType();

  String getRemarks();

  void addColumn(IColumn column);

  List<IColumn> getColumns();
}
