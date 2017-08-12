package org.benhur.codurance.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.benhur.codurance.data.Database;
import org.benhur.codurance.data.IDatabase;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;

public class Server implements IServer {

  private final IDatabase database;

  public Server() {
    this.database = new Database();
  }

  @Override
  public IMessage postMessage(String senderName, String message) {
    return database.addMessage(senderName, message);
  }

  @Override
  public void followUser(String userName, String followedUserName) {
    database.getOrCreateUser(userName).addFollowee(database.getOrCreateUser(followedUserName));
  }

  @Override
  public List<IMessage> getTimeline(String userName) {
    return database.getMessages(userName);
  }

  @Override
  public List<IMessage> getWall(String userName) {
    List<IMessage> messages = new ArrayList<>();
    IUser user = database.getOrCreateUser(userName);
    messages.addAll(database.getMessages(user.getName()));
    for (IUser followee : user.getFollowees()) {
      messages.addAll(database.getMessages(followee.getName()));
    }
    return messages
        .stream()
        .sorted((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()))
        .collect(Collectors.toList());
  }
}
