package org.benhur.utility.database.dependencies.dsm;

import java.io.IOException;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.database.dependencies.configuration.Configuration;

public class MainDSM
{
  public static void main(String[] args)
  {
    try
    {
      Configuration configuration = new Configuration("config/projectdependencies.properties");
      DatabaseBuilder databaseBuilder = new DatabaseBuilder();
      IDatabase database = databaseBuilder.buildDatabase(configuration.getHost(), configuration.getPort(),
                                                         configuration.getUsername(), configuration.getPassword(),
                                                         configuration.getDatabaseName());

      DatabaseDSMFileBuilder databaseDSMFileBuilder = new DatabaseDSMFileBuilder();
      databaseDSMFileBuilder.createDSMFile(database, configuration, database.getName() + ".dsm");
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
