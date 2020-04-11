// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class DatabaseUtility {
  public static List<ITable> getAllTables(IDatabase database) {
    List<ITable> allTables = new ArrayList<>();
    for (ICatalog catalog : database.getCatalogs()) {
      for (ISchema schema : catalog.getSchemas()) {
        allTables.addAll(schema.getTables());
      }
    }
    return allTables;
  }
}
