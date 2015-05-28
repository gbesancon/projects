package org.benhur.utility.database.dependencies.dsm;

import java.io.IOException;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.database.dependencies.configuration.Configuration;

public class MainDatabaseDependenciesDSM
{
  public static void main(String[] args)
  {
    if (args.length == 1)
    {
      try
      {
        Configuration configuration = new Configuration(args[0]);
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
    else
    {
      System.err.println("No configuration file specified");
    }
  }
}
