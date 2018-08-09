// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.dsm;

import java.util.List;
import java.util.Map;
import org.benhur.utility.database.dependencies.DatabaseUtility;
import org.benhur.utility.database.dependencies.IColumn;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.configuration.Configuration;
import org.benhur.utility.dsm.ADSMFileBuilder;
import org.benhur.utility.dsm.Edge;
import org.benhur.utility.dsm.Node;

public class DatabaseDSMFileBuilder extends ADSMFileBuilder<IDatabase, Configuration> {
  @Override
  protected void buildDSMFile(
      IDatabase input, Configuration configuration, Map<String, Node> nodeByIds, List<Edge> edges) {
    List<ITable> tables = DatabaseUtility.getAllTables(input);
    for (ITable table : tables) {
      createNode(table.getId(), table.getName(), nodeByIds);
    }
    for (ITable table : tables) {
      for (IColumn column : table.getColumns()) {
        for (IColumn referedColumn : column.getReferedColumns()) {
          createEdge(column.getTable().getId(), referedColumn.getTable().getId(), nodeByIds, edges);
        }
      }
    }
  }
}
