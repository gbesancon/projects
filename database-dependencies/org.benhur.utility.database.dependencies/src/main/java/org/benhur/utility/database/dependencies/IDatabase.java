// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.List;

public interface IDatabase {
  String getId();

  String getName();

  void addCatalog(ICatalog catalog);

  List<ICatalog> getCatalogs();
}
