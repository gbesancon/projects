package org.benhur.utility.database.dependencies.dgml;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.database.dependencies.group.Group;
import org.benhur.utility.database.dependencies.group.GroupBuilder;
import org.benhur.utility.database.dependencies.group.GroupUtility;
import org.benhur.utility.dgml.Category;
import org.benhur.utility.dgml.DGMLFileWriter;
import org.benhur.utility.dgml.DirectedGraph;
import org.benhur.utility.dgml.Link;
import org.benhur.utility.dgml.Node;
import org.benhur.utility.io.PropertiesFileReader;

public class MainDGML
{
  public static void main(String[] args)
  {
    try
    {
      String host = PropertiesFileReader.getStringProperty("config/projectdependencies.properties", "HOST");
      int port = PropertiesFileReader.getIntProperty("config/projectdependencies.properties", "PORT");
      String username = PropertiesFileReader.getStringProperty("config/projectdependencies.properties", "USERNAME");
      String password = PropertiesFileReader.getStringProperty("config/projectdependencies.properties", "PASSWORD");
      String databaseName = PropertiesFileReader.getStringProperty("config/projectdependencies.properties",
                                                                   "DATABASE_NAME");
      DatabaseBuilder databaseBuilder = new DatabaseBuilder();
      IDatabase database = databaseBuilder.buildDatabase(host, port, username, password, databaseName);

      createDGMLFile(database, "graph.dgml");
    }
    catch (PropertyException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  protected static void createDGMLFile(IDatabase database, String dgmlFilepath) throws IOException
  {
    DGMLFileWriter dgmlFileWriter = new DGMLFileWriter();
    DirectedGraph directedGraph = createDirectedGraph(database);
    dgmlFileWriter.writeToFile(directedGraph, dgmlFilepath);
  }

  protected static DirectedGraph createDirectedGraph(IDatabase database)
  {
    DirectedGraph directedGraph = new DirectedGraph();

    Map<String, Node> nodeByIds = new HashMap<>();

    GroupBuilder groupBuilder = new GroupBuilder();
    Group group = groupBuilder.buildGroup(database);
    List<Group> allGroups = GroupUtility.getAllGroups(group);

    for (Group aGroup : allGroups)
    {
      createGroupNode(aGroup, directedGraph, nodeByIds);
      for (ITable solutionItem : aGroup.getTables())
      {
        createTableNode(solutionItem, directedGraph, nodeByIds);
      }
    }

    Category containsCategory = new Category("Contains", "Contains",
        "Whether the source of the link contains the target object", false, true, "Contained By", true, "Contains");
    directedGraph.categories.add(containsCategory);

    for (Group aGroup : allGroups)
    {
      createGroupTableLinks(aGroup, directedGraph, containsCategory, nodeByIds);
      createGroupSubGroupLinks(aGroup, directedGraph, containsCategory, nodeByIds);
      for (ITable solutionItem : aGroup.getTables())
      {
        createTableDependenciesLinks(solutionItem, directedGraph, nodeByIds);
      }
    }

    return directedGraph;
  }

  protected static void createGroupNode(Group group, DirectedGraph directedGraph, Map<String, Node> nodeByIds)
  {
    Node node = new Node(group.getId(), group.getName(), true, true);
    nodeByIds.put(node.id, node);
    directedGraph.nodes.add(node);
  }

  protected static void createTableNode(ITable table, DirectedGraph directedGraph, Map<String, Node> nodeByIds)
  {
    Node node = new Node(table.getId(), table.getName(), false, false);
    nodeByIds.put(node.id, node);
    directedGraph.nodes.add(node);
  }

  protected static void createGroupTableLinks(Group group, DirectedGraph directedGraph,
      Category containsCategory, Map<String, Node> nodeByIds)
  {
    for (ITable table : group.getTables())
    {
      Node source = nodeByIds.get(group.getId());
      Node target = nodeByIds.get(table.getId());
      Link link = new Link(source, target, containsCategory);
      directedGraph.links.add(link);
    }
  }

  protected static void createGroupSubGroupLinks(Group group, DirectedGraph directedGraph, Category containsCategory,
      Map<String, Node> nodeByIds)
  {
    for (Group subGroup : group.getSubGroups())
    {
      Node source = nodeByIds.get(group.getId());
      Node target = nodeByIds.get(subGroup.getId());
      Link link = new Link(source, target, containsCategory);
      directedGraph.links.add(link);
    }
  }

  protected static void createTableDependenciesLinks(ITable table, DirectedGraph directedGraph,
      Map<String, Node> nodeByIds)
  {
    for (ITable dependency : table.getDependencies())
    {
      Node source = nodeByIds.get(table.getId());
      Node target = nodeByIds.get(dependency.getId());
      Link link = new Link(source, target, null);
      directedGraph.links.add(link);
    }
  }
}
