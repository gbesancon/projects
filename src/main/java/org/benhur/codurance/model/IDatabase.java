package org.benhur.codurance.model;

import java.util.List;

public interface IDatabase {

  List<IUser> getUsers();

  IUser getUser(String name);

  List<IMessage> getMessages();

  List<IMessage> getMessages(IUser user);
}
