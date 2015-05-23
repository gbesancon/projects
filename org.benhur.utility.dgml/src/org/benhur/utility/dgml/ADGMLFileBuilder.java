package org.benhur.utility.dgml;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class ADGMLFileBuilder<TInput>
{
  public void createDGMLFile(TInput input, String dgmlFilepath) throws IOException
  {
    DGMLFileWriter dgmlFileWriter = new DGMLFileWriter();
    DirectedGraph directedGraph = createDirectedGraph(input);
    dgmlFileWriter.writeToFile(directedGraph, dgmlFilepath);
  }

  protected DirectedGraph createDirectedGraph(TInput input)
  {
    DirectedGraph directedGraph = new DirectedGraph();
    Map<String, Node> nodeByIds = new HashMap<>();
    buildDirectedGraph(input, directedGraph, nodeByIds);
    return directedGraph;
  }

  protected abstract void buildDirectedGraph(TInput input, DirectedGraph directedGraph, Map<String, Node> nodeByIds);

  protected void createNode(String id, String name, boolean isGroup, boolean isExpanded, DirectedGraph directedGraph,
      Map<String, Node> nodeByIds)
  {
    Node node = new Node(id, name, isGroup, isExpanded);
    nodeByIds.put(node.id, node);
    directedGraph.nodes.add(node);
  }

  protected void createLink(String sourceId, String targetId, DirectedGraph directedGraph, Category category,
      Map<String, Node> nodeByIds)
  {
    Node source = nodeByIds.get(sourceId);
    Node target = nodeByIds.get(targetId);
    Link link = new Link(source, target, category);
    directedGraph.links.add(link);
  }
}
