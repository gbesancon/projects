// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ISchema {
  ICatalog getCatalog();

  String getId();

  String getName();

  void addTable(ITable table);

  List<ITable> getTables();
}
