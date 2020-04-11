// Copyright (C) 2017 GBesancon

package org.benhur.utility.dsm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ADSMFileBuilder<TInput, TConfiguration> {
  public void createDSMFile(TInput input, TConfiguration configuration, String dsmFilepath)
      throws IOException {
    Map<String, Node> nodeByIds = new HashMap<>();
    List<Edge> edges = new ArrayList<>();
    buildDSMFile(input, configuration, nodeByIds, edges);
    createDSMFile(nodeByIds, edges, dsmFilepath);
    createDSMMatrix(dsmFilepath);
  }

  protected abstract void buildDSMFile(
      TInput input, TConfiguration configuration, Map<String, Node> nodeByIds, List<Edge> edges);

  protected void createNode(String id, String name, Map<String, Node> nodeByIds) {
    Node node = new Node(id, name);
    nodeByIds.put(node.getId(), node);
  }

  protected void createEdge(
      String sourceId, String targetId, Map<String, Node> nodeByIds, List<Edge> edges) {
    Node source = nodeByIds.get(sourceId);
    Node target = nodeByIds.get(targetId);
    Edge edge = new Edge(source, target);
    edges.add(edge);
  }

  protected void createDSMFile(Map<String, Node> nodeByIds, List<Edge> edges, String dsmFilepath)
      throws IOException {
    FileWriter fileWriter = new FileWriter(dsmFilepath);
    List<Node> nodes = new ArrayList<>();
    for (Node node : nodeByIds.values()) {
      fileWriter.append((nodes.size() + 1) + " " + node.getName() + "\n");
      nodes.add(node);
    }
    fileWriter.append("dep\n");
    for (Edge edge : edges) {
      Node source = edge.getSource();
      Node target = edge.getTarget();
      fileWriter.append((nodes.indexOf(source) + 1) + " " + (nodes.indexOf(target) + 1) + "\n");
    }
    fileWriter.close();
  }

  protected void createDSMMatrix(String dsmFilepath) {
    List<String> dsmFilepathes = new ArrayList<>();
    dsmFilepathes.add(dsmFilepath);
    // DSM dsm = new DSM(dsmFilepathes);

  }
}
