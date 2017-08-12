package org.benhur.codurance.data;

public class User implements IUser {

  private final String name;

  public User(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
