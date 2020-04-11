// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.PropertyException;
import org.benhur.utility.database.dependencies.DatabaseUtility;
import org.benhur.utility.database.dependencies.ICatalog;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ISchema;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.configuration.Configuration;
import org.benhur.utility.regex.RegExUtility;

public class DatabaseGroupBuilder {

  public Group buildGroup(IDatabase database, Configuration configuration) {
    return buildGroup(database, DatabaseUtility.getAllTables(database), configuration);
  }

  public Group buildGroup(IDatabase database, List<ITable> tables, Configuration configuration) {
    Group group =
        new Group(
            database.getId(),
            database.getName(),
            table -> table.getSchema().getCatalog().getDatabase() == database);
    List<Group> subGroups = createGroupsFromConfiguration(new int[0], configuration);
    if (subGroups.isEmpty()) {
      subGroups = createGroupsFromDatabase(database);
    }
    for (Group subGroup : subGroups) {
      group.addSubGroup(subGroup);
    }
    for (ITable table : tables) {
      addTableToGroup(group, table);
    }
    return group;
  }

  protected List<Group> createGroupsFromConfiguration(int[] groupIds, Configuration configuration) {
    List<Group> subGroups = new ArrayList<>();
    int iSubGroup = 1;
    while (true) {
      int[] subGroupIds = new int[groupIds.length + 1];
      for (int iId = 0; iId < groupIds.length; iId++) {
        subGroupIds[iId] = groupIds[iId];
      }
      subGroupIds[groupIds.length] = iSubGroup;

      Group subGroup = createGroupFromConfiguration(subGroupIds, configuration);
      if (subGroup != null) {
        subGroups.add(subGroup);
        for (Group subSubGroup : createGroupsFromConfiguration(subGroupIds, configuration)) {
          subGroup.addSubGroup(subSubGroup);
        }
      } else {
        break;
      }
      iSubGroup++;
    }
    return subGroups;
  }

  protected Group createGroupFromConfiguration(int[] ids, Configuration configuration) {
    Group group = null;
    try {
      String groupName = configuration.getGroupName(ids);
      if (!groupName.equalsIgnoreCase("")) {
        String groupProjectNameIncludePatternString =
            configuration.getGroupTableNameIncludePattern(ids);
        String groupProjectNameExcludePatternString =
            configuration.getGroupTableNameExcludePattern(ids);
        group =
            new Group(
                groupName,
                groupName,
                table ->
                    RegExUtility.checkValue(
                        table.getName(),
                        groupProjectNameIncludePatternString,
                        groupProjectNameExcludePatternString));
      }
    } catch (PropertyException e) {
      // We expect an exception to leave the loop.
    }
    return group;
  }

  protected List<Group> createGroupsFromDatabase(IDatabase database) {
    List<Group> groups = new ArrayList<>();
    for (ICatalog catalog : database.getCatalogs()) {
      Group catalogGroup =
          new Group(
              database.getId() + "_" + catalog.getId(),
              catalog.getName(),
              table -> table.getSchema().getCatalog() == catalog);
      for (ISchema schema : catalog.getSchemas()) {
        Group schemaGroup =
            new Group(schema.getId(), schema.getName(), table -> table.getSchema() == schema);
        List<String> tableTypes = new ArrayList<>();
        for (ITable table : schema.getTables()) {
          if (!tableTypes.contains(table.getType())) {
            tableTypes.add(table.getType());
          }
        }
        for (String tableType : tableTypes) {
          Group tableTypeGroup =
              new Group(
                  schema.getId() + "_" + tableType,
                  tableType,
                  table -> table.getType().equalsIgnoreCase(tableType));
          schemaGroup.addSubGroup(tableTypeGroup);
        }
        catalogGroup.addSubGroup(schemaGroup);
      }
      groups.add(catalogGroup);
    }
    return groups;
  }

  protected boolean addTableToGroup(Group group, ITable table) {
    boolean foundMatch = false;
    if (group.acceptTable(table)) {
      boolean matchSubGroup = false;
      for (Group subGroup : group.getSubGroups()) {
        matchSubGroup |= addTableToGroup(subGroup, table);
      }
      if (!matchSubGroup) {
        group.addTable(table);
      }
      foundMatch = true;
    }
    return foundMatch;
  }
}
