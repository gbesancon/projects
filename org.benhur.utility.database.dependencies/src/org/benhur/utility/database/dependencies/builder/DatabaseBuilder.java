package org.benhur.utility.database.dependencies.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.benhur.utility.database.dependencies.Column;
import org.benhur.utility.database.dependencies.Database;
import org.benhur.utility.database.dependencies.IColumn;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.Table;

public class DatabaseBuilder
{
  public IDatabase buildDatabase(String host, int port, String username, String password, String databaseName)
  {
    IDatabase database = null;

    Connection connection = null;
    ResultSet tableResultSet = null;
    ResultSet columnResultSet = null;
    ResultSet foreignColumnResultSet = null;

    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?" + "user="
          + username + "&password=" + password);

      database = new Database(databaseName, databaseName);

      Map<String, ITable> tableByIds = new HashMap<>();
      Map<String, IColumn> columnByIds = new HashMap<>();

      tableResultSet = connection.getMetaData().getTables(null, null, "%", null);
      while (tableResultSet.next())
      {
        String tableName = tableResultSet.getString(3);
        ITable table = getTable(database, tableName, tableByIds);
        columnResultSet = connection.getMetaData().getColumns(null, null, tableName, "%");
        while (columnResultSet.next())
        {
          String columnName = columnResultSet.getString(4);
          IColumn column = getColumn(table, columnName, columnByIds);
          table.addColumn(column);
        }
        database.addTable(table);
      }

      for (ITable table : tableByIds.values())
      {
        foreignColumnResultSet = connection.getMetaData().getImportedKeys(null, null, table.getName());
        while (foreignColumnResultSet.next())
        {
          String foreignKeyTableName = foreignColumnResultSet.getString(7);
          ITable foreignKeyTable = getTable(database, foreignKeyTableName, tableByIds);
          String foreignKeyColumnName = foreignColumnResultSet.getString(8);
          IColumn foreignKeyColumn = getColumn(foreignKeyTable, foreignKeyColumnName, columnByIds);
          String primaryKeyTableName = foreignColumnResultSet.getString(3);
          ITable primaryKeyTable = getTable(database, primaryKeyTableName, tableByIds);
          String primaryKeyColumnName = foreignColumnResultSet.getString(4);
          IColumn primaryKeyColumn = getColumn(primaryKeyTable, primaryKeyColumnName, columnByIds);
          foreignKeyColumn.setForeignColumn(primaryKeyColumn);
        }
      }
    }
    catch (Exception e)
    {}
    finally
    {
      try
      {
        if (tableResultSet != null)
        {
          tableResultSet.close();
        }
        if (columnResultSet != null)
        {
          columnResultSet.close();
        }
        if (foreignColumnResultSet != null)
        {
          foreignColumnResultSet.close();
        }
        if (connection != null)
        {
          connection.close();
        }
      }
      catch (Exception e)
      {}
    }
    return database;
  }

  protected ITable getTable(IDatabase database, String tableName, Map<String, ITable> tableByIds)
  {
    ITable table = null;
    String id = database.getId() + "_" + tableName;
    if (tableByIds.containsKey(id))
    {
      table = tableByIds.get(id);
    }
    else
    {
      table = new Table(id, tableName);
      tableByIds.put(table.getId(), table);
    }
    return table;
  }

  protected IColumn getColumn(ITable table, String columnName, Map<String, IColumn> columnByIds)
  {
    IColumn column = null;
    String id = table.getId() + "_" + columnName;
    if (columnByIds.containsKey(id))
    {
      column = columnByIds.get(id);
    }
    else
    {
      column = new Column(id, columnName);
      columnByIds.put(column.getId(), column);
    }
    return column;
  }
}
