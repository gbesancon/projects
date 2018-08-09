// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.benhur.utility.database.dependencies.ITable;

public class Group {
  protected final String id;
  protected final String name;
  protected final Function<ITable, Boolean> acceptTable;
  protected final List<ITable> tables = new ArrayList<>();
  protected final List<Group> subGroups = new ArrayList<>();

  public Group(String id, String name, Function<ITable, Boolean> acceptTable) {
    this.id = id;
    this.name = name;
    this.acceptTable = acceptTable;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public boolean acceptTable(ITable table) {
    return this.acceptTable.apply(table);
  }

  public void addTable(ITable table) {
    tables.add(table);
  }

  public List<ITable> getTables() {
    return tables;
  }

  public void addSubGroup(Group subGroup) {
    subGroups.add(subGroup);
  }

  public List<Group> getSubGroups() {
    return subGroups;
  }
}
