package org.benhur.codurance.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database implements IDatabase {

  private List<IUser> users = new ArrayList<>();
  private Map<String, IUser> usersByName = new HashMap<>();
  private List<IMessage> messages = new ArrayList<>();
  private Map<IUser, List<IMessage>> messagesByUser = new HashMap<>();

  @Override
  public List<IUser> getUsers() {
    return users;
  }

  @Override
  public IUser getOrCreateUser(String name) {
    final IUser user;
    if (usersByName.containsKey(name)) {
      user = usersByName.get(name);
    } else {
      user = new User(name);
      users.add(user);
      usersByName.put(name, user);
      messagesByUser.put(user, new ArrayList<>());
    }
    return user;
  }

  @Override
  public List<IMessage> getMessages() {
    return messages;
  }

  @Override
  public List<IMessage> getMessages(String userName) {
    return messagesByUser.get(getOrCreateUser(userName));
  }

  @Override
  public IMessage addMessage(String senderName, String text) {
    IUser sender = getOrCreateUser(senderName);
    IMessage message = new Message(messages.size(), sender, new Date(), text);
    messages.add(message);
    messagesByUser.get(sender).add(message);
    return message;
  }
}
