// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.dgml;

import java.io.IOException;

import javax.xml.bind.PropertyException;

import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.database.dependencies.configuration.Configuration;
import org.benhur.utility.dgml.DGMLException;

public class MainDatabaseDependenciesDGML
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

        DatabaseDGMLFileBuilder databaseDGMLFileBuilder = new DatabaseDGMLFileBuilder();
        databaseDGMLFileBuilder.createDGMLFile(database, configuration, database.getName() + ".dgml");
      }
      catch (PropertyException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      catch (DGMLException e)
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
