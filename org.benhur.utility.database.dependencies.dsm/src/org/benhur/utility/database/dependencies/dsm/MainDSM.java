package org.benhur.utility.database.dependencies.dsm;

import java.io.IOException;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.io.PropertiesFileReader;

public class MainDSM
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

      DatabaseDSMFileBuilder databaseDSMFileBuilder = new DatabaseDSMFileBuilder();
      databaseDSMFileBuilder.createDSMFile(database, database.getName() + ".dsm");
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
}
