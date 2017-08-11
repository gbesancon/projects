package org.benhur.codurance.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database implements IDatabase {

  private Map<IUser, Collection<IMessage>> messagesByUser = new HashMap<>();

  @Override
  public Collection<IUser> getUsers() {
    return messagesByUser.keySet();
  }

  @Override
  public IUser getUser(String name) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<IMessage> getMessages() {
    Collection<IMessage> messages = new ArrayList<>();
    messagesByUser.values().forEach(messages::addAll);
    return messages;
  }

  @Override
  public Collection<IMessage> getMessages(IUser user) {
    // TODO Auto-generated method stub
    return null;
  }
}
