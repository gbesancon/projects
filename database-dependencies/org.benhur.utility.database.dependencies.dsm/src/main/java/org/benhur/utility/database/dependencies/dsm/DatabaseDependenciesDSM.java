// Copyright (C) 2017 GBesancon

package org.benhur.utility.database.dependencies.dsm;

import java.io.IOException;
import javax.xml.bind.PropertyException;
import org.benhur.utility.database.dependencies.IDatabase;
import org.benhur.utility.database.dependencies.builder.DatabaseBuilder;
import org.benhur.utility.database.dependencies.configuration.Configuration;

public class DatabaseDependenciesDSM {
  public static void main(String[] args) {
    if (args.length == 1) {
      try {
        Configuration configuration = new Configuration(args[0]);
        System.out.println("Building a memory representation of the database.");
        DatabaseBuilder databaseBuilder = new DatabaseBuilder();
        IDatabase database =
            databaseBuilder.buildDatabase(
                configuration.getDatabaseType(),
                configuration.getHost(),
                configuration.getPort(),
                configuration.getUsername(),
                configuration.getPassword(),
                configuration.getDatabaseName());
        System.out.println("Memory representation of the database built.");

        System.out.println("Creating a DSM file representation of the database.");
        DatabaseDSMFileBuilder databaseDSMFileBuilder = new DatabaseDSMFileBuilder();
        databaseDSMFileBuilder.createDSMFile(database, configuration, database.getName() + ".dsm");
        System.out.println("DSM file representation of the database created.");
      } catch (PropertyException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.err.println("No configuration file specified");
    }
  }
}