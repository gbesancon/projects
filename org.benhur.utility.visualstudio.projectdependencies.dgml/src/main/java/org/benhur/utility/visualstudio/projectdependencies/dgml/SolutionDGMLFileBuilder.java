// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.dgml;

import java.util.List;
import java.util.Map;

import javax.xml.bind.PropertyException;

import org.benhur.utility.dgml.ADGMLFileBuilder;
import org.benhur.utility.dgml.Category;
import org.benhur.utility.dgml.DGMLException;
import org.benhur.utility.dgml.DirectedGraph;
import org.benhur.utility.dgml.Node;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;
import org.benhur.utility.visualstudio.projectdependencies.dgml.configuration.Configuration;
import org.benhur.utility.visualstudio.projectdependencies.group.Group;
import org.benhur.utility.visualstudio.projectdependencies.group.GroupBuilder;
import org.benhur.utility.visualstudio.projectdependencies.group.GroupUtility;

public class SolutionDGMLFileBuilder extends ADGMLFileBuilder<ISolution, Configuration>
{
  @Override
  protected void buildDirectedGraph(ISolution input, Configuration configuration, DirectedGraph directedGraph,
      Map<String, Node> nodeByIds) throws DGMLException
  {
    GroupBuilder groupBuilder = new GroupBuilder();
    Group group = groupBuilder.buildGroup(input, configuration);
    List<Group> allGroups = GroupUtility.getAllGroups(group);

    for (Group aGroup : allGroups)
    {
      createNode(getId(aGroup, configuration), aGroup.getName(), true, true, directedGraph, nodeByIds);
      for (ISolutionItem aSolutionItem : aGroup.getProjects())
      {
        createNode(getId(aSolutionItem, configuration), aSolutionItem.getName(), false, false, directedGraph,
                   nodeByIds);
      }
    }

    Category containsCategory = new Category("Contains", "Contains",
        "Whether the source of the link contains the target object", false, true, "Contained By", true, "Contains");
    directedGraph.categories.add(containsCategory);

    for (Group aGroup : allGroups)
    {
      for (Group aSubGroup : aGroup.getSubGroups())
      {
        createLink(getId(aGroup, configuration), getId(aSubGroup, configuration), directedGraph, containsCategory,
                   nodeByIds);
      }
      for (ISolutionItem aSolutionItem : aGroup.getProjects())
      {
        createLink(getId(aGroup, configuration), getId(aSolutionItem, configuration), directedGraph, containsCategory,
                   nodeByIds);
        for (ISolutionItem aProjectDependency : aSolutionItem.getProjectDependencies())
        {
          createLink(getId(aSolutionItem, configuration), getId(aProjectDependency, configuration), directedGraph, null,
                     nodeByIds);
        }
      }
    }
  }

  protected String getId(Group group, Configuration configuration)
  {
    boolean useNameAsId = false;
    try
    {
      useNameAsId = configuration.getUseNameAsId();
    }
    catch (PropertyException e)
    {
      e.printStackTrace();
    }
    return "Group" + computeId(useNameAsId ? group.getName() : group.getId());
  }

  protected String getId(ISolutionItem solutionItem, Configuration configuration)
  {
    boolean useNameAsId = false;
    try
    {
      useNameAsId = configuration.getUseNameAsId();
    }
    catch (PropertyException e)
    {
      e.printStackTrace();
    }
    return computeId(useNameAsId ? solutionItem.getName() : solutionItem.getId());
  }

  protected String computeId(String id)
  {
    String dgmlId = id;
    dgmlId = dgmlId.replace(':', '-');
    dgmlId = dgmlId.replace('\\', '-');
    dgmlId = dgmlId.replace('/', '-');
    dgmlId = dgmlId.replace('<', '-');
    dgmlId = dgmlId.replace('>', '-');
    return dgmlId;
  }

}
