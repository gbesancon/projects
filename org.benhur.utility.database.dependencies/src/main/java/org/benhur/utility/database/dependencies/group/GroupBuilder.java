// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.PropertyException;
import org.benhur.utility.database.dependencies.DatabaseUtility;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.configuration.Configuration;

public class GroupBuilder {
  public Group buildGroup(IDatabase database, Configuration configuration) {
    Group group = createGroup(database, configuration);
    populateGroupWithDatabase(group, database);
    return group;
  }

  protected Group createGroup(IDatabase database, Configuration configuration) {
    Group group = new Group(database.getName(), "", "");
    for (Group subGroup : computeSubGroups(group, new int[0], configuration)) {
      group.addSubGroup(subGroup);
    }
    return group;
  }

  protected List<Group> computeSubGroups(Group group, int[] groupIds, Configuration configuration) {
    List<Group> subGroups = new ArrayList<>();
    int iSubGroup = 1;
    while (true) {
      int[] subGroupIds = new int[groupIds.length + 1];
      for (int iId = 0; iId < groupIds.length; iId++) {
        subGroupIds[iId] = groupIds[iId];
      }
      subGroupIds[groupIds.length] = iSubGroup;

      Group subGroup = getGroup(subGroupIds, configuration);
      if (subGroup != null) {
        subGroups.add(subGroup);
        for (Group subSubGroup : computeSubGroups(subGroup, subGroupIds, configuration)) {
          subGroup.addSubGroup(subSubGroup);
        }
      } else {
        break;
      }
      iSubGroup++;
    }
    return subGroups;
  }

  protected Group getGroup(int[] ids, Configuration configuration) {
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
                groupProjectNameIncludePatternString,
                groupProjectNameExcludePatternString);
      }
    } catch (PropertyException e) {
      // We expect an exception to leave the loop.
    }
    return group;
  }

  protected void populateGroupWithDatabase(Group group, IDatabase database) {
    for (ITable table : DatabaseUtility.getAllTables(database)) {
      populateGroupWithTable(group, table);
    }
  }

  protected boolean populateGroupWithTable(Group group, ITable table) {
    boolean foundMatch = false;
    if (group.acceptTable(table)) {
      boolean matchSubGroup = false;
      for (Group subGroup : group.getSubGroups()) {
        matchSubGroup |= populateGroupWithTable(subGroup, table);
      }
      if (!matchSubGroup) {
        group.addTable(table);
      }
      foundMatch = true;
    }
    return foundMatch;
  }
}
