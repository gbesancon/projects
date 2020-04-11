// Copyright (C) 2017 GBesancon

package org.benhur.utility.visualstudio.projectdependencies.group;

import java.util.ArrayList;
import java.util.List;
import org.benhur.utility.regex.RegExUtility;
import org.benhur.utility.visualstudio.projectdependencies.ISolutionItem;

public class Group {
  protected final String name;
  protected final String includePatternString;
  protected final String excludePatternString;
  protected final List<ISolutionItem> solutionItems = new ArrayList<>();
  protected final List<Group> subGroups = new ArrayList<>();

  public Group(String name, String includePatternString, String excludePatternString) {
    this.name = name;
    this.includePatternString = includePatternString;
    this.excludePatternString = excludePatternString;
  }

  public String getId() {
    return name;
  }

  public String getName() {
    return name;
  }

  public boolean acceptSolutionItem(ISolutionItem solutionItem) {
    return RegExUtility.checkValue(
        solutionItem.getName(), includePatternString, excludePatternString);
  }

  public void addSolutionItem(ISolutionItem solutionItem) {
    solutionItems.add(solutionItem);
  }

  public List<ISolutionItem> getProjects() {
    return solutionItems;
  }

  public void addSubGroup(Group subGroup) {
    subGroups.add(subGroup);
  }

  public List<Group> getSubGroups() {
    return subGroups;
  }
}
