// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.builder;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.benhur.utility.database.dependencies.Catalog;
import org.benhur.utility.database.dependencies.Column;
import org.benhur.utility.database.dependencies.Database;
import org.benhur.utility.database.dependencies.ICatalog;
import org.benhur.utility.database.dependencies.IColumn;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.ISchema;
import org.benhur.utility.database.dependencies.ITable;
import org.benhur.utility.database.dependencies.Schema;
import org.benhur.utility.database.dependencies.Table;

public class DatabaseBuilder {
  public IDatabase buildDatabase(
      String type, String host, int port, String username, String password, String databaseName) {

    IDatabase database = null;

    String connectionURL = null;
    Connection connection = null;
    Statement statement = null;
    ResultSet tableResultSet = null;
    ResultSet columnResultSet = null;
    ResultSet foreignColumnResultSet = null;
    ResultSet viewColumnUsageResultSet = null;

    try {
      switch (type) {
        case "mysql":
          Class.forName("com.mysql.jdbc.Driver");
          connectionURL =
              "jdbc:mysql://"
                  + host
                  + (port != 0 ? ":" + port : "")
                  + "/"
                  + databaseName
                  + "?"
                  + "user="
                  + username
                  + "&"
                  + "password="
                  + password;
          break;
        case "sqlserver":
          // http://www.codejava.net/java-se/jdbc/connect-to-microsoft-sql-server-via-jdbc
          Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
          String[] hostInstance = host.split("\\\\");
          connectionURL =
              "jdbc:sqlserver://"
                  + hostInstance[0]
                  + (port != 0 ? ":" + port : "")
                  + (hostInstance.length > 1 ? ";" + "instanceName=" + hostInstance[1] : "")
                  + ";"
                  + "databaseName="
                  + databaseName
                  + ";"
                  + "user="
                  + username
                  + ";"
                  + "password="
                  + password;
          break;
        default:
          throw new Exception("Unknown database type.");
      }

      //System.out.println(connectionURL);
      //DriverManager.setLogWriter(new PrintWriter(System.out));
      connection = DriverManager.getConnection(connectionURL);
      if (connection != null) {
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        System.out.println("Driver name: " + databaseMetaData.getDriverName());
        System.out.println("Driver version: " + databaseMetaData.getDriverVersion());
        System.out.println("Product name: " + databaseMetaData.getDatabaseProductName());
        System.out.println("Product version: " + databaseMetaData.getDatabaseProductVersion());

        database = new Database(databaseName, databaseName);

        Map<String, ICatalog> catalogByIds = new HashMap<>();
        Map<String, ISchema> schemaByIds = new HashMap<>();
        Map<String, ITable> tableByIds = new HashMap<>();
        Map<String, IColumn> columnByIds = new HashMap<>();

        tableResultSet = connection.getMetaData().getTables(null, null, "%", null);
        while (tableResultSet.next()) {
          String tableCatalog = tableResultSet.getString("TABLE_CAT");
          ICatalog catalog = getCatalog(database, tableCatalog, catalogByIds);
          String tableSchema = tableResultSet.getString("TABLE_SCHEM");
          ISchema schema = getSchema(catalog, tableSchema, schemaByIds);
          String tableName = tableResultSet.getString("TABLE_NAME");
          String tableType = tableResultSet.getString("TABLE_TYPE");
          String remarks = tableResultSet.getString("REMARKS");
          ITable table = getTable(schema, tableName, tableType, remarks, tableByIds);
          columnResultSet = connection.getMetaData().getColumns(null, null, tableName, "%");
          while (columnResultSet.next()) {
            String columnName = columnResultSet.getString("COLUMN_NAME");
            IColumn column = getColumn(table, columnName, columnByIds);
          }
        }

        for (ITable table : tableByIds.values()) {
          foreignColumnResultSet =
              connection.getMetaData().getImportedKeys(null, null, table.getName());
          while (foreignColumnResultSet.next()) {
            String primaryKeyCatalogName = foreignColumnResultSet.getString("PKTABLE_CAT");
            ICatalog primaryKeyCatalog = getCatalog(database, primaryKeyCatalogName, catalogByIds);
            String primaryKeySchemaName = foreignColumnResultSet.getString("PKTABLE_SCHEM");
            ISchema primaryKeySchema =
                getSchema(primaryKeyCatalog, primaryKeySchemaName, schemaByIds);
            String primaryKeyTableName = foreignColumnResultSet.getString("PKTABLE_NAME");
            ITable primaryKeyTable =
                getTable(primaryKeySchema, primaryKeyTableName, null, null, tableByIds);
            String primaryKeyColumnName = foreignColumnResultSet.getString("PKCOLUMN_NAME");
            IColumn primaryKeyColumn =
                getColumn(primaryKeyTable, primaryKeyColumnName, columnByIds);
            String foreignKeyCatalogName = foreignColumnResultSet.getString("FKTABLE_CAT");
            ICatalog foreignKeyCatalog = getCatalog(database, foreignKeyCatalogName, catalogByIds);
            String foreignKeySchemaName = foreignColumnResultSet.getString("FKTABLE_SCHEM");
            ISchema foreignKeySchema =
                getSchema(foreignKeyCatalog, foreignKeySchemaName, schemaByIds);
            String foreignKeyTableName = foreignColumnResultSet.getString("FKTABLE_NAME");
            ITable foreignKeyTable =
                getTable(foreignKeySchema, foreignKeyTableName, null, null, tableByIds);
            String foreignKeyColumnName = foreignColumnResultSet.getString("FKCOLUMN_NAME");
            IColumn foreignKeyColumn =
                getColumn(foreignKeyTable, foreignKeyColumnName, columnByIds);
            foreignKeyColumn.refersTo(primaryKeyColumn);
          }
        }

        statement = connection.createStatement();
        viewColumnUsageResultSet =
            statement.executeQuery("SELECT * FROM INFORMATION_SCHEMA.VIEW_COLUMN_USAGE");

        while (viewColumnUsageResultSet.next()) {
          String viewCatalogName = viewColumnUsageResultSet.getString("VIEW_CATALOG");
          ICatalog viewCatalog = getCatalog(database, viewCatalogName, catalogByIds);
          String viewSchemaName = viewColumnUsageResultSet.getString("VIEW_SCHEMA");
          ISchema viewSchema = getSchema(viewCatalog, viewSchemaName, schemaByIds);
          String viewName = viewColumnUsageResultSet.getString("VIEW_NAME");
          ITable view = getTable(viewSchema, viewName, null, null, tableByIds);
          String tableCatalogName = viewColumnUsageResultSet.getString("TABLE_CATALOG");
          ICatalog tableCatalog = getCatalog(database, tableCatalogName, catalogByIds);
          String tableSchemaName = viewColumnUsageResultSet.getString("TABLE_SCHEMA");
          ISchema tableSchema = getSchema(tableCatalog, tableSchemaName, schemaByIds);
          String tableName = viewColumnUsageResultSet.getString("TABLE_NAME");
          ITable table = getTable(tableSchema, tableName, null, null, tableByIds);
          String columnName = viewColumnUsageResultSet.getString("COLUMN_NAME");
          IColumn viewColumn = getColumn(view, columnName, columnByIds);
          IColumn tableColumn = getColumn(table, columnName, columnByIds);
          viewColumn.refersTo(tableColumn);
        }
      }
    } catch (Exception e) {
    } finally {
      try {
        if (viewColumnUsageResultSet != null) {
          viewColumnUsageResultSet.close();
        }
        if (tableResultSet != null) {
          tableResultSet.close();
        }
        if (columnResultSet != null) {
          columnResultSet.close();
        }
        if (foreignColumnResultSet != null) {
          foreignColumnResultSet.close();
        }
        if (statement != null) {
          statement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (Exception e) {
      }
    }
    return database;
  }

  protected ICatalog getCatalog(
      IDatabase database, String name, Map<String, ICatalog> catalogByIds) {
    ICatalog catalog = null;
    String id = database.getId() + "_" + name;
    if (catalogByIds.containsKey(id)) {
      catalog = catalogByIds.get(id);
    } else {
      catalog = new Catalog(database, id, name);
      database.addCatalog(catalog);
      catalogByIds.put(catalog.getId(), catalog);
    }
    return catalog;
  }

  protected ISchema getSchema(ICatalog catalog, String name, Map<String, ISchema> schemaById) {
    ISchema schema = null;
    String id = catalog.getId() + "_" + name;
    if (schemaById.containsKey(id)) {
      schema = schemaById.get(id);
    } else {
      schema = new Schema(catalog, id, name);
      catalog.addSchema(schema);
      schemaById.put(schema.getId(), schema);
    }
    return schema;
  }

  protected ITable getTable(
      ISchema schema, String name, String type, String remarks, Map<String, ITable> tableByIds) {
    ITable table = null;
    String id = schema.getId() + "_" + name;
    if (tableByIds.containsKey(id)) {
      table = tableByIds.get(id);
    } else {
      table = new Table(schema, id, name, type, remarks);
      schema.addTable(table);
      tableByIds.put(table.getId(), table);
    }
    return table;
  }

  protected IColumn getColumn(ITable table, String name, Map<String, IColumn> columnByIds) {
    IColumn column = null;
    String id = table.getId() + "_" + name;
    if (columnByIds.containsKey(id)) {
      column = columnByIds.get(id);
    } else {
      column = new Column(table, id, name);
      table.addColumn(column);
      columnByIds.put(column.getId(), column);
    }
    return column;
  }
}
