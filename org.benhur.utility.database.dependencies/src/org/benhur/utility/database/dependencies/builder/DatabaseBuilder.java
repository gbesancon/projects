package org.benhur.utility.database.dependencies.builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.benhur.utility.database.dependencies.Database;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ITable;

public class DatabaseBuilder
{
  public IDatabase buildDatabase(String host, int port, String username, String password, String databaseName)
  {
    IDatabase database = null;

    Connection connection = null;
    ResultSet resultSet = null;

    try
    {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName + "?" + "user="
          + username + "&password=" + password);

      database = new Database(databaseName);

      Map<String, ITable> tableByNames = new HashMap<>();

      Set<String> tableNames = new HashSet<>();
      resultSet = connection.getMetaData().getTables(null, null, "%", null);
      while (resultSet.next())
      {
        String tableName = resultSet.getString(3);
        tableNames.add(tableName);
        getTable(tableName, tableByNames);
      }

      Set<String> foreignKeyTables = new HashSet<>();
      for (String tableName : tableNames)
      {
        ITable table = getTable(tableName, tableByNames);
        resultSet = connection.getMetaData().getImportedKeys(null, null, tableName);
        while (resultSet.next())
        {
          String importedTableName = resultSet.getString(3);
          foreignKeyTables.add(importedTableName);
          ITable importedTable = getTable(importedTableName, tableByNames);
          table.addDependency(importedTable);
        }
      }
    }
    catch (Exception e)
    {}
    finally
    {
      try
      {
        if (resultSet != null)
        {
          resultSet.close();
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

  protected ITable getTable(String tableName, Map<String, ITable> tables)
  {
    return null;
  }
}
