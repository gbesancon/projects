// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.benhur.utility.visualstudio.projectdependencies.IProject;
import org.benhur.utility.visualstudio.projectdependencies.ISolution;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;

public class SolutionUtility {
  private SolutionUtility() {}

  public static List<ISolutionItem> getAlphabeticallyOrderedSolutionItems(ISolution solution) {
    Map<String, ISolutionItem> solutionItemByIds = new HashMap<>();
    populateSolutionItemsById(solutionItemByIds, solution);
    List<ISolutionItem> solutionItems = new ArrayList<>(solutionItemByIds.values());
    Collections.sort(
        solutionItems,
        new Comparator<ISolutionItem>() {
          @Override
          public int compare(ISolutionItem o1, ISolutionItem o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
          }
        });
    return solutionItems;
  }

  protected static void populateSolutionItemsById(
      Map<String, ISolutionItem> solutionItemByIds, ISolution solution) {
    for (IProject project : solution.getProjects()) {
      populateSolutionItemsById(solutionItemByIds, project);
    }
  }

  protected static void populateSolutionItemsById(
      Map<String, ISolutionItem> solutionItemByIds, ISolutionItem solutionItem) {
    if (!solutionItemByIds.containsKey(solutionItem.getId())) {
      solutionItemByIds.put(solutionItem.getId(), solutionItem);
    }

    for (ISolutionItem projectDependency : solutionItem.getProjectDependencies()) {
      populateSolutionItemsById(solutionItemByIds, projectDependency);
    }
  }
}
