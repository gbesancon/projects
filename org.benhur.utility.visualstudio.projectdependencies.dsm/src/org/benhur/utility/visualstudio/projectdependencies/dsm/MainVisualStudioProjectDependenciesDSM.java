package org.benhur.utility.visualstudio.projectdependencies.dsm;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.PropertyException;

import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.builder.SolutionBuilder;
import org.benhur.utility.visualstudio.projectdependencies.configuration.Configuration;
import org.benhur.utility.visualstudio.projectdependencies.filter.SolutionFilterer;

public class MainVisualStudioProjectDependenciesDSM
{
  public static void main(String[] args)
  {
    if (args.length == 1)
    {
      try
      {
        Configuration configuration = new Configuration(args[0]);
        File solutionFile = new File(configuration.getSolutionFile());
        SolutionBuilder solutionBuilder = new SolutionBuilder();
        ISolution solution = solutionBuilder.buildSolution(solutionFile);
        SolutionFilterer solutionFilterer = new SolutionFilterer();
        ISolution filteredSolution = solutionFilterer.filterSolution(solution, configuration);

        SolutionDSMFileBuilder solutionDSMFileBuilder = new SolutionDSMFileBuilder();
        solutionDSMFileBuilder.createDSMFile(filteredSolution, configuration, solution.getName() + ".dsm");
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
