// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Database implements IDatabase {
  protected final String id;
  protected final String name;
  protected final List<ICatalog> catalogs;

  public Database(String id, String name) {
    this.id = id;
    this.name = name;
    this.catalogs = new ArrayList<>();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addCatalog(ICatalog catalog) {
    catalogs.add(catalog);
  }

  @Override
  public List<ICatalog> getCatalogs() {
    return catalogs;
  }
}
