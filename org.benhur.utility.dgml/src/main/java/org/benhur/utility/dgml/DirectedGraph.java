package org.benhur.utility.dgml;

import java.util.ArrayList;
import java.util.List;

public class DirectedGraph
{
  public final List<Node> nodes = new ArrayList<>();
  public final List<Link> links = new ArrayList<>();
  public final List<Category> categories = new ArrayList<>();

  public DirectedGraph()
  {}
}
