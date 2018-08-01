// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.dgml;

import java.util.List;
import java.util.Map;
import org.benhur.utility.database.dependencies.IColumn;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.configuration.Configuration;
import org.benhur.utility.database.dependencies.group.Group;
import org.benhur.utility.database.dependencies.group.GroupBuilder;
import org.benhur.utility.database.dependencies.group.GroupUtility;
import org.benhur.utility.dgml.ADGMLFileBuilder;
import org.benhur.utility.dgml.Category;
import org.benhur.utility.dgml.DGMLException;
import org.benhur.utility.dgml.DirectedGraph;
import org.benhur.utility.dgml.Node;

public class DatabaseDGMLFileBuilder extends ADGMLFileBuilder<IDatabase, Configuration> {
  @Override
  protected void buildDirectedGraph(
      IDatabase input,
      Configuration configuration,
      DirectedGraph directedGraph,
      Map<String, Node> nodeByIds)
      throws DGMLException {
    GroupBuilder groupBuilder = new GroupBuilder();
    Group group = groupBuilder.buildGroup(input, configuration);
    List<Group> allGroups = GroupUtility.getAllGroups(group);

    for (Group aGroup : allGroups) {
      createNode(aGroup.getId(), aGroup.getName(), true, true, directedGraph, nodeByIds);
      for (ITable aTable : aGroup.getTables()) {
        createNode(aTable.getId(), aTable.getName(), true, true, directedGraph, nodeByIds);
        for (IColumn aColumn : aTable.getColumns()) {
          createNode(aColumn.getId(), aColumn.getName(), false, false, directedGraph, nodeByIds);
        }
      }
    }

    Category containsCategory =
        new Category(
            "Contains",
            "Contains",
            "Whether the source of the link contains the target object",
            false,
            true,
            "Contained By",
            true,
            "Contains");
    directedGraph.categories.add(containsCategory);

    for (Group aGroup : allGroups) {
      for (Group aSubGroup : aGroup.getSubGroups()) {
        createLink(aGroup.getId(), aSubGroup.getId(), directedGraph, containsCategory, nodeByIds);
      }
      for (ITable aTable : aGroup.getTables()) {
        createLink(aGroup.getId(), aTable.getId(), directedGraph, containsCategory, nodeByIds);
        for (IColumn aColumn : aTable.getColumns()) {
          createLink(aTable.getId(), aColumn.getId(), directedGraph, containsCategory, nodeByIds);
          if (aColumn.getForeignColumn() != null) {
            createLink(
                aColumn.getId(),
                aColumn.getForeignColumn().getId(),
                directedGraph,
                null,
                nodeByIds);
          }
        }
      }
    }
  }
}
