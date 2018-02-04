// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportedProject extends AProjectDependency
{
  protected final File file;

  public ImportedProject(ISolution solution, File file)
  {
    super(solution, file.getAbsolutePath(), computeName(file));
    this.file = file;
  }

  public File getFile()
  {
    return file;
  }

  protected static String computeName(File file)
  {
    String name = null;
    if (file.exists())
    {
      final Pattern pattern = Pattern.compile("<_PropertySheetDisplayName>(.+?)</_PropertySheetDisplayName>");
      try (BufferedReader br = new BufferedReader(new FileReader(file)))
      {
        for (String line; (line = br.readLine()) != null && name == null;)
        {
          final Matcher matcher = pattern.matcher(line);
          if (matcher.find())
          {
            name = matcher.group(1);
          }
        }
      }
      catch (FileNotFoundException e)
      {
        e.printStackTrace();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    if (name == null)
    {
      name = file.getName();
    }
    return name;
  }
}
