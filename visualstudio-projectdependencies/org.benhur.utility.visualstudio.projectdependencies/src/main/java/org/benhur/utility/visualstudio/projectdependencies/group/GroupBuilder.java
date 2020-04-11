// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.group;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.PropertyException;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;
import org.benhur.utility.visualstudio.projectdependencies.builder.SolutionUtility;
import org.benhur.utility.visualstudio.projectdependencies.configuration.Configuration;

public class GroupBuilder {
  public Group buildGroup(ISolution solution, Configuration configuration) {
    Group group = createGroup(solution, configuration);
    populateGroupWithSolution(group, solution);
    return group;
  }

  protected Group createGroup(ISolution solution, Configuration configuration) {
    Group group = new Group(solution.getName(), "", "");
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
            configuration.getGroupProjectNameIncludePattern(ids);
        String groupProjectNameExcludePatternString =
            configuration.getGroupProjectNameExcludePattern(ids);
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

  protected void populateGroupWithSolution(Group group, ISolution solution) {
    for (ISolutionItem solutionItem :
        SolutionUtility.getAlphabeticallyOrderedSolutionItems(solution)) {
      populateGroupWithSolutionItem(group, solutionItem);
    }
  }

  protected boolean populateGroupWithSolutionItem(Group group, ISolutionItem solutionItem) {
    boolean foundMatch = false;
    if (group.acceptSolutionItem(solutionItem)) {
      boolean matchSubGroup = false;
      for (Group subGroup : group.getSubGroups()) {
        matchSubGroup |= populateGroupWithSolutionItem(subGroup, solutionItem);
      }
      if (!matchSubGroup) {
        group.addSolutionItem(solutionItem);
      }
      foundMatch = true;
    }
    return foundMatch;
  }
}
