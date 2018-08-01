// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.regex.RegExUtility;

public class Group {
  protected final String name;
  protected final String includePatternString;
  protected final String excludePatternString;
  protected final List<ITable> tables = new ArrayList<>();
  protected final List<Group> subGroups = new ArrayList<>();

  public Group(String name, String includePatternString, String excludePatternString) {
    this.name = name;
    this.includePatternString = includePatternString;
    this.excludePatternString = excludePatternString;
  }

  public String getId() {
    return name;
  }

  public String getName() {
    return name;
  }

  public boolean acceptTable(ITable table) {
    return RegExUtility.checkValue(table.getName(), includePatternString, excludePatternString);
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
