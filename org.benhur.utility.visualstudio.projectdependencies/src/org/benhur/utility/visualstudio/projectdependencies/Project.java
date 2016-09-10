package org.benhur.utility.visualstudio.projectdependencies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.benhur.utility.file.FileUtility;

public class Project extends AProjectDependency implements IProject
{
  protected final File file;

  public Project(ISolution solution, File file)
  {
    super(solution, file.getAbsolutePath(), computeName(file));
    this.file = file;
  }

  @Override
  public File getFile()
  {
    return file;
  }

  protected static String computeName(File file)
  {
    String name = null;
    if (file.exists())
    {
      final Pattern pattern = Pattern.compile("<ProjectName>(.+?)</ProjectName>");
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
      name = FileUtility.getBaseFilename(file.getName());
    }
    return name;
  }
}
