package org.benhur.codurance.data;

import java.util.List;

public interface IUser {

  String getName();

  List<IUser> getFollowees();

  void addFollowee(IUser followeeUser);
}
