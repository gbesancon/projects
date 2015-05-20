package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.io.PropertiesFileReader;

public class GroupBuilder
{
  public Group buildGroup(IDatabase database)
  {
    Group group = createGroup(database);
    populateGroupWithDatabase(group, database);
    return group;
  }

  protected Group createGroup(IDatabase database)
  {
    Group group = new Group(database.getName(), "", "");
    for (Group subGroup : computeSubGroups(group, new int[0]))
    {
      group.addSubGroup(subGroup);
    }
    return group;
  }

  protected List<Group> computeSubGroups(Group group, int[] groupIds)
  {
    List<Group> subGroups = new ArrayList<>();
    int iSubGroup = 1;
    while (true)
    {
      int[] subGroupIds = new int[groupIds.length + 1];
      for (int iId = 0; iId < groupIds.length; iId++)
      {
        subGroupIds[iId] = groupIds[iId];
      }
      subGroupIds[groupIds.length] = iSubGroup;

      Group subGroup = getGroup(subGroupIds);
      if (subGroup != null)
      {
        subGroups.add(subGroup);
        for (Group subSubGroup : computeSubGroups(subGroup, subGroupIds))
        {
          subGroup.addSubGroup(subSubGroup);
        }
      }
      else
      {
        break;
      }
      iSubGroup++;
    }
    return subGroups;
  }

  protected Group getGroup(int[] ids)
  {
    Group group = null;
    try
    {
      String groupPrefix = "GROUP_";
      for (int i = 0; i < ids.length; i++)
      {
        groupPrefix += ids[i] + "_";
      }
      String groupName = PropertiesFileReader.getStringProperty("config/projectdependencies.properties", groupPrefix
          + "NAME");
      if (!groupName.equalsIgnoreCase(""))
      {
        String groupProjectNameIncludePatternString = PropertiesFileReader
            .getStringProperty("config/projectdependencies.properties", groupPrefix + "TABLE_NAME_INCLUDE_PATTERN");
        String groupProjectNameExcludePatternString = PropertiesFileReader
            .getStringProperty("config/projectdependencies.properties", groupPrefix + "TABLE_NAME_EXCLUDE_PATTERN");
        group = new Group(groupName, groupProjectNameIncludePatternString, groupProjectNameExcludePatternString);
      }
    }
    catch (PropertyException e)
    {
      // We expect an exception to leave the loop.
    }
    return group;
  }

  protected void populateGroupWithDatabase(Group group, IDatabase database)
  {
    for (ITable table : database.getTables())
    {
      populateGroupWithTable(group, table);
    }
  }

  protected boolean populateGroupWithTable(Group group, ITable table)
  {
    boolean foundMatch = false;
    if (group.acceptTable(table))
    {
      boolean matchSubGroup = false;
      for (Group subGroup : group.getSubGroups())
      {
        matchSubGroup |= populateGroupWithTable(subGroup, table);
      }
      if (!matchSubGroup)
      {
        group.addTable(table);
      }
      foundMatch = true;
    }
    return foundMatch;
  }
}
