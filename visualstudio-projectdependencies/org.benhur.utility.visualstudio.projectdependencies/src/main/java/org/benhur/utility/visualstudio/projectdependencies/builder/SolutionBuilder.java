// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.builder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.benhur.utility.visualstudio.projectdependencies.IProject;
import org.benhur.utility.visualstudio.projectdependencies.IProjectDependency;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ImportedProject;
import org.benhur.utility.visualstudio.projectdependencies.Project;
import org.benhur.utility.visualstudio.projectdependencies.ProjectReference;
import org.benhur.utility.visualstudio.projectdependencies.Reference;
import org.benhur.utility.visualstudio.projectdependencies.Solution;

public class SolutionBuilder {
  protected final Map<String, IProject> projectByFilepathes = new HashMap<>();
  protected final Map<IProject, ProjectReference> projectReferenceByProjects = new HashMap<>();
  protected final Map<String, Reference> referenceByName = new HashMap<>();
  protected final Map<String, ImportedProject> importedProjectByFilepathes = new HashMap<>();

  public ISolution buildSolution(File file) {
    Solution solution = new Solution(file);
    for (IProject project : computeProjects(solution)) {
      solution.addProject(project);
    }
    return solution;
  }

  protected Set<IProject> computeProjects(ISolution solution) {
    Set<IProject> projects = new HashSet<>();
    final Pattern pattern =
        Pattern.compile("^Project[(]\"([^\"]*)\"[)] = \"([^\"]*)\", \"([^\"]*)\", \"([^\"]*)\"");
    try (BufferedReader br = new BufferedReader(new FileReader(solution.getFile()))) {
      for (String line; (line = br.readLine()) != null; ) {
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
          String projectFileString = matcher.group(3);
          File projectFile = getSolutionFile(solution, solution.getFile(), projectFileString);
          if (projectFile != null) {
            projects.add(getProject(solution, projectFile));
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return projects;
  }

  protected Set<IProjectDependency> computeProjectDependencies(ISolution solution, File file) {
    Set<IProjectDependency> projectDependencies = new HashSet<>();
    if (file.isFile()) {
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        for (String line; (line = br.readLine()) != null; ) {
          Pattern importProjectPattern =
              Pattern.compile("(.+?)<Import Project=\"([^\"]*)\"(.+?)/>");
          final Matcher importProjectMatcher = importProjectPattern.matcher(line);
          if (importProjectMatcher.find()) {
            String aImportProject = importProjectMatcher.group(2);
            File importedProjectFile = getSolutionFile(solution, file, aImportProject);
            if (importedProjectFile != null) {
              projectDependencies.add(getImportedProject(solution, importedProjectFile));
            }
          } else {
            // Reference
            Pattern referenceIncludePattern =
                Pattern.compile("(.+?)<Reference Include=\"([^\",]*)(,.*)?\"(.*)>");
            final Matcher referenceIncludeMatcher = referenceIncludePattern.matcher(line);
            if (referenceIncludeMatcher.find()) {
              String referenceInclude = referenceIncludeMatcher.group(2);
              projectDependencies.add(getReference(solution, referenceInclude));
            } else {
              // Project Reference
              Pattern projectReferencePattern =
                  Pattern.compile("(.+?)<ProjectReference Include=\"([^\"]*)\"(.*)>");
              final Matcher projectReferenceMatcher = projectReferencePattern.matcher(line);
              if (projectReferenceMatcher.find()) {
                String projectRefenceInclude = projectReferenceMatcher.group(2);
                File projectReferencedFile = getSolutionFile(solution, file, projectRefenceInclude);
                if (projectReferencedFile != null) {
                  projectDependencies.add(
                      getProjectReference(getProject(solution, projectReferencedFile)));
                }
              } else {
                // DLL
                Pattern additionalDependenciesPattern =
                    Pattern.compile("(.+?)<AdditionalDependencies>(.*)</AdditionalDependencies>");
                final Matcher additionalDependenciesMatcher =
                    additionalDependenciesPattern.matcher(line);
                if (additionalDependenciesMatcher.find()) {
                  String additionalDependencies = additionalDependenciesMatcher.group(2);
                  String[] additionalDependencyArray = additionalDependencies.split(";");
                  for (String additionalDependency : additionalDependencyArray) {
                    if (!additionalDependency.equalsIgnoreCase("%(AdditionalDependencies)")) {
                      projectDependencies.add(getReference(solution, additionalDependency));
                    }
                  }
                }
              }
            }
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return projectDependencies;
  }

  protected File getSolutionFile(ISolution solution, File referenceFile, String filepath) {
    File file = null;
    filepath =
        filepath.replace(
            "$(SolutionDir)",
            solution.getFile().getParentFile().getAbsolutePath() + File.separator);
    filepath = filepath.replace("$(Configuration)", "Debug");
    filepath = filepath.replace("$(Platform)", "x64");
    file = new File(filepath);
    if (!file.isFile()) {
      file = new File(referenceFile.getParentFile(), filepath);
      if (!file.isFile()) {
        file = new File(solution.getFile().getParentFile(), filepath);
        if (!file.isFile()) {
          file = null;
        }
      }
    }

    if (file != null) {
      try {
        file = file.getCanonicalFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return file;
  }

  protected IProject getProject(ISolution solution, File file) {
    IProject project = null;
    if (projectByFilepathes.containsKey(file.getAbsolutePath())) {
      project = projectByFilepathes.get(file.getAbsolutePath());
    } else {
      project = new Project(solution, file);
      for (IProjectDependency projectDependency : computeProjectDependencies(solution, file)) {
        project.addProjectDependency(projectDependency);
      }
      projectByFilepathes.put(file.getAbsolutePath(), project);
    }
    return project;
  }

  protected IProjectDependency getProjectReference(IProject project) {
    ProjectReference projectReference = null;
    if (projectReferenceByProjects.containsKey(project)) {
      projectReference = projectReferenceByProjects.get(project);
    } else {
      projectReference = new ProjectReference(project);
      projectReferenceByProjects.put(project, projectReference);
    }
    return projectReference;
  }

  protected IProjectDependency getReference(ISolution solution, String name) {
    Reference reference = null;
    if (referenceByName.containsKey(name)) {
      reference = referenceByName.get(name);
    } else {
      reference = new Reference(solution, name);
      referenceByName.put(name, reference);
    }
    return reference;
  }

  protected IProjectDependency getImportedProject(ISolution solution, File file) {
    ImportedProject importedProject = null;
    if (importedProjectByFilepathes.containsKey(file.getAbsolutePath())) {
      importedProject = importedProjectByFilepathes.get(file.getAbsolutePath());
    } else {
      importedProject = new ImportedProject(solution, file);
      for (IProjectDependency projectDependency : computeProjectDependencies(solution, file)) {
        importedProject.addProjectDependency(projectDependency);
      }
      importedProjectByFilepathes.put(file.getAbsolutePath(), importedProject);
    }
    return importedProject;
  }
}
