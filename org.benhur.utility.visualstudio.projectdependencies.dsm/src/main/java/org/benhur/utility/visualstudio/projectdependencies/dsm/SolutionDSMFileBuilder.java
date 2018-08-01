// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.dsm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.benhur.utility.dsm.ADSMFileBuilder;
import org.benhur.utility.dsm.Edge;
import org.benhur.utility.dsm.Node;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;
import org.benhur.utility.visualstudio.projectdependencies.configuration.Configuration;
import org.benhur.utility.visualstudio.projectdependencies.group.Group;
import org.benhur.utility.visualstudio.projectdependencies.group.GroupBuilder;
import org.benhur.utility.visualstudio.projectdependencies.group.GroupUtility;

public class SolutionDSMFileBuilder extends ADSMFileBuilder<ISolution, Configuration> {
  @Override
  protected void buildDSMFile(
      ISolution input, Configuration configuration, Map<String, Node> nodeByIds, List<Edge> edges) {
    GroupBuilder groupBuilder = new GroupBuilder();
    Group group = groupBuilder.buildGroup(input, configuration);

    List<ISolutionItem> solutionItems = GetSolutionItems(group);
    for (ISolutionItem solutionItem : solutionItems) {
      createNode(solutionItem.getId(), solutionItem.getName(), nodeByIds);
    }
    for (ISolutionItem solutionItem : solutionItems) {
      for (ISolutionItem projectDependency : solutionItem.getProjectDependencies()) {
        createEdge(solutionItem.getId(), projectDependency.getId(), nodeByIds, edges);
      }
    }
  }

  protected List<ISolutionItem> GetSolutionItems(Group group) {
    List<ISolutionItem> solutionItems = new ArrayList<>();
    List<Group> allGroups = GroupUtility.getAllGroups(group);
    for (Group aGroup : allGroups) {
      for (ISolutionItem aSolutionItem : aGroup.getProjects()) {
        solutionItems.add(aSolutionItem);
      }
    }
    return solutionItems;
  }
}
