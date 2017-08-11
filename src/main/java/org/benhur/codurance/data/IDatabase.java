package org.benhur.codurance.data;

import java.util.Collection;

public interface IDatabase {

  Collection<IUser> getUsers();

  IUser getUser(String name);

  Collection<IMessage> getMessages();

  Collection<IMessage> getMessages(IUser user);
}
