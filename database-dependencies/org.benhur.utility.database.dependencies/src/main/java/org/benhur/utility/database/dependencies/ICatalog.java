// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.List;

public interface ICatalog {
  IDatabase getDatabase();

  String getId();

  String getName();

  void addSchema(ISchema schema);

  List<ISchema> getSchemas();
}
