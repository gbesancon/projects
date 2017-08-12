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
    followees.add(followee);
  }

  @Override
  public String toString() {
    return name;
  }
}
