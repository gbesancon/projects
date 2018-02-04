// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.group;

import java.util.ArrayList;
import java.util.List;

public class GroupUtility
{
  public static List<Group> getAllGroups(Group group)
  {
    List<Group> allGroups = new ArrayList<>();
    getAllGroups(group, allGroups);
    return allGroups;
  }

  protected static void getAllGroups(Group group, List<Group> groups)
  {
    groups.add(group);
    for (Group subGroup : group.getSubGroups())
    {
      getAllGroups(subGroup, groups);
    }
  }
}
