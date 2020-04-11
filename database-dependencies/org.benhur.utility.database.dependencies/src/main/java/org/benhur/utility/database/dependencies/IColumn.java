// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.Collection;

public interface IColumn {
  ITable getTable();

  String getId();

  String getName();

  void refersTo(IColumn referedColumn);

  Collection<IColumn> getReferedColumns();
}
