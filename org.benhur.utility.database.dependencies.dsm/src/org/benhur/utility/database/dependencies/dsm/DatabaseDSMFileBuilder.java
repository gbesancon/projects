package org.benhur.utility.database.dependencies.dsm;

import java.util.List;
import java.util.Map;

import org.benhur.utility.database.dependencies.IColumn;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.dsm.ADSMFileBuilder;
import org.benhur.utility.dsm.Edge;
import org.benhur.utility.dsm.Node;

public class DatabaseDSMFileBuilder extends ADSMFileBuilder<IDatabase>
{
  @Override
  protected void buildDSMFile(IDatabase input, Map<String, Node> nodeByIds, List<Edge> edges)
  {
    for (ITable table : input.getTables())
    {
      createNode(table.getId(), table.getName(), nodeByIds);
    }
    for (ITable table : input.getTables())
    {
      for (IColumn column : table.getColumns())
      {
        if (column.getForeignColumn() != null)
        {
          createEdge(column.getTable().getId(), column.getForeignColumn().getTable().getId(), nodeByIds, edges);
        }
      }
    }
  }
}
