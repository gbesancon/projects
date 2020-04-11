// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies;

import java.util.ArrayList;
import java.util.List;

public class Catalog implements ICatalog {
  protected final IDatabase database;
  protected final String id;
  protected final String name;
  protected final List<ISchema> schemas;

  public Catalog(IDatabase database, String id, String name) {
    this.database = database;
    this.id = id;
    this.name = name;
    this.schemas = new ArrayList<>();
  }

  @Override
  public IDatabase getDatabase() {
    return database;
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
  public void addSchema(ISchema schema) {
    schemas.add(schema);
  }

  @Override
  public List<ISchema> getSchemas() {
    return schemas;
  }
}
