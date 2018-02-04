// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.filter;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.PropertyException;

import org.benhur.utility.regex.RegExUtility;
import org.benhur.utility.visualstudio.projectdependencies.IProject;
import org.benhur.utility.visualstudio.projectdependencies.IProjectDependency;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;
import org.benhur.utility.visualstudio.projectdependencies.ImportedProject;
import org.benhur.utility.visualstudio.projectdependencies.Project;
import org.benhur.utility.visualstudio.projectdependencies.ProjectReference;
import org.benhur.utility.visualstudio.projectdependencies.Reference;
import org.benhur.utility.visualstudio.projectdependencies.Solution;
import org.benhur.utility.visualstudio.projectdependencies.configuration.Configuration;

public class SolutionFilterer
{
  protected final Map<String, IProject> projectByFilepathes = new HashMap<>();
  protected final Map<IProject, ProjectReference> projectReferenceByProjects = new HashMap<>();
  protected final Map<String, Reference> referenceByName = new HashMap<>();
  protected final Map<String, ImportedProject> importedProjectByFilepathes = new HashMap<>();

  public ISolution filterSolution(ISolution solution, Configuration configuration)
  {
    Solution filteredSolution = new Solution(solution.getFile());
    for (IProject filteredProject : filterProject(solution, configuration, filteredSolution))
    {
      filteredSolution.addProject(filteredProject);
    }
    return filteredSolution;
  }

  protected Set<IProject> filterProject(ISolution solution, Configuration configuration, ISolution filteredSolution)
  {
    Set<IProject> filteredProjects = new HashSet<>();
    for (IProject project : solution.getProjects())
    {
      if (acceptProjectName(project.getName(), configuration))
      {
        IProject filteredProject = getProject(filteredSolution, project.getFile());
        for (IProjectDependency filteredProjectDependency : filterProjectDependencies(project, configuration,
                                                                                      filteredSolution))
        {
          filteredProject.addProjectDependency(filteredProjectDependency);
        }
        filteredProjects.add(filteredProject);
      }
    }
    return filteredProjects;
  }

  protected Set<IProjectDependency> filterProjectDependencies(ISolutionItem solutionItem, Configuration configuration,
      ISolution filteredSolution)
  {
    Set<IProjectDependency> projectDependencies = new HashSet<>();
    for (IProjectDependency projectDependency : solutionItem.getProjectDependencies())
    {
      if (acceptProjectName(projectDependency.getName(), configuration))
      {
        if (projectDependency instanceof Project)
        {
          projectDependencies.add(new Project(filteredSolution, ((Project) projectDependency).getFile()));
        }
        else if (projectDependency instanceof ImportedProject)
        {
          projectDependencies
              .add(new ImportedProject(filteredSolution, ((ImportedProject) projectDependency).getFile()));
        }
        else if (projectDependency instanceof ProjectReference)
        {
          projectDependencies.add(new ProjectReference(getProject(filteredSolution,
                                                                  ((ProjectReference) projectDependency).getFile())));
        }
        else if (projectDependency instanceof Reference)
        {
          projectDependencies.add(new Reference(filteredSolution, ((Reference) projectDependency).getName()));
        }
      }
      else
      {
        for (IProjectDependency projectDependency2 : filterProjectDependencies(projectDependency, configuration,
                                                                               filteredSolution))
        {
          projectDependencies.add(projectDependency2);
        }
      }
    }
    return projectDependencies;
  }

  protected boolean acceptProjectName(String projectName, Configuration configuration)
  {
    boolean acceptProjectName = false;
    try
    {
      String projectNameIncludePatternString = configuration.getProjectNameIncludePattern();
      String projectNameExcludePatternString = configuration.getProjectNameExcludePattern();
      acceptProjectName = RegExUtility.checkValue(projectName, projectNameIncludePatternString,
                                                  projectNameExcludePatternString);
    }
    catch (PropertyException e)
    {
      e.printStackTrace();
    }
    return acceptProjectName;
  }

  protected IProject getProject(ISolution solution, File file)
  {
    IProject project = null;
    if (projectByFilepathes.containsKey(file.getAbsolutePath()))
    {
      project = projectByFilepathes.get(file.getAbsolutePath());
    }
    else
    {
      project = new Project(solution, file);
      projectByFilepathes.put(file.getAbsolutePath(), project);
    }
    return project;
  }

  protected IProjectDependency getProjectReference(IProject project)
  {
    ProjectReference projectReference = null;
    if (projectReferenceByProjects.containsKey(project))
    {
      projectReference = projectReferenceByProjects.get(project);
    }
    else
    {
      projectReference = new ProjectReference(project);
      projectReferenceByProjects.put(project, projectReference);
    }
    return projectReference;
  }

  protected IProjectDependency getReference(ISolution solution, String name)
  {
    Reference reference = null;
    if (referenceByName.containsKey(name))
    {
      reference = referenceByName.get(name);
    }
    else
    {
      reference = new Reference(solution, name);
      referenceByName.put(name, reference);
    }
    return reference;
  }

  protected IProjectDependency getImportedProject(ISolution solution, File file)
  {
    ImportedProject importedProject = null;
    if (importedProjectByFilepathes.containsKey(file.getAbsolutePath()))
    {
      importedProject = importedProjectByFilepathes.get(file.getAbsolutePath());
    }
    else
    {
      importedProject = new ImportedProject(solution, file);
      importedProjectByFilepathes.put(file.getAbsolutePath(), importedProject);
    }
    return importedProject;
  }
}
