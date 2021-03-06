// Copyright (C) 2017 GBesancon

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
  public IUser followUser(String userName, String followedUserName) {
    IUser user = database.getOrCreateUser(userName);
    user.addFollowee(database.getOrCreateUser(followedUserName));
    return user;
  }

  @Override
  public List<IMessage> getTimeline(String userName) {
    return database
        .getMessages(userName)
        .stream()
        .sorted((o1, o2) -> -Long.compare(o1.getId(), o2.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public List<IMessage> getWall(String userName) {
    List<IMessage> wall = new ArrayList<>();
    IUser user = database.getOrCreateUser(userName);
    wall.addAll(database.getMessages(user.getName()));
    for (IUser followee : user.getFollowees()) {
      wall.addAll(database.getMessages(followee.getName()));
    }
    return wall.stream()
        .sorted((o1, o2) -> -Long.compare(o1.getId(), o2.getId()))
        .collect(Collectors.toList());
  }
}
