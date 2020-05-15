// Copyright (C) 2017 GBesancon

package org.benhur.utility.dgml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DGMLFileWriter {
  // https://msdn.microsoft.com/library/ee842619.aspx#DGML
  public void writeToFile(DirectedGraph directedGraph, String dgmlFilepath) throws IOException {
    FileWriter fileWriter = new FileWriter(new File(dgmlFilepath));
    fileWriter.write("<?xml version='1.0' encoding='utf-8'?>\n");
    fileWriter.write("<DirectedGraph xmlns=\"http://schemas.microsoft.com/vs/2009/dgml\">\n");
    fileWriter.write("\t<Nodes>\n");
    for (Node node : directedGraph.nodes) {
      fileWriter.write("\t\t<Node Id=\"" + node.id + "\"");
      if (node.isGroup) {
        if (node.isExpanded) {
          fileWriter.write((" Group=\"Expanded\""));
        } else {
          fileWriter.write((" Group=\"Collapsed\""));
        }
      }
      fileWriter.write(" Label=\"" + node.name + "\"" + "/>");
      fileWriter.write("\n");
    }
    fileWriter.write("\t</Nodes>\n");
    fileWriter.write("\t<Links>\n");
    for (Link link : directedGraph.links) {
      fileWriter.write(
          "\t\t<Link Source=\"" + link.source.id + "\"" + " Target=\"" + link.target.id + "\"");
      if (link.category != null) {
        fileWriter.write(" Category=\"" + link.category.id + "\"");
      }
      fileWriter.write("/>\n");
    }
    fileWriter.write("\t</Links>\n");
    fileWriter.write("\t<Categories>\n");
    for (Category category : directedGraph.categories) {
      fileWriter.write(
          "\t\t<Category Id=\""
              + category.id
              + "\" Label=\""
              + category.label
              + "\" Description=\""
              + category.description
              + "\"");
      if (category.canBeDataDriven) {
        fileWriter.write(" CanBeDataDriven=\"True\"");
      } else {
        fileWriter.write(" CanBeDataDriven=\"False\"");
      }
      if (category.canLinkedNodesBeDataDriven) {
        fileWriter.write(" CanLinkedNodesBeDataDriven=\"True\"");
      } else {
        fileWriter.write(" CanLinkedNodesBeDataDriven=\"False\"");
      }
      fileWriter.write(" IncomingActionLabel=\"" + category.incomingActionLabel + "\"");
      if (category.isContainment) {
        fileWriter.write(" IsContainment=\"True\"");
      } else {
        fileWriter.write(" IsContainment=\"False\"");
      }
      fileWriter.write(" OutgoingActionLabel=\"" + category.outgoingActionLabel + "\"");
      fileWriter.write(" />\n");
    }
    fileWriter.write("\t</Categories>\n");
    fileWriter.write("</DirectedGraph>");
    fileWriter.write('\n');
    fileWriter.close();
  }
}
