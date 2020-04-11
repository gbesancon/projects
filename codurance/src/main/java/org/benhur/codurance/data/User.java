// Copyright (C) 2017 GBesancon

package org.benhur.codurance.data;

import java.util.ArrayList;
import java.util.List;

public class User implements IUser {

  private final String name;
  private final List<IUser> followees;

  public User(String name) {
    this.name = name;
    this.followees = new ArrayList<>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<IUser> getFollowees() {
    return followees;
  }

  @Override
  public void addFollowee(IUser followee) {
    if (!followees.contains(followee)) {
      followees.add(followee);
    }
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(name);
    if (!followees.isEmpty()) {
      stringBuilder.append(" follows ");
      followees.forEach(followee -> stringBuilder.append(followee.getName() + ","));
      stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }
    return stringBuilder.toString();
  }
}
