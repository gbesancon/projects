// Copyright (C) 2017 GBesancon

package org.benhur.codurance.server;

import java.util.List;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;

public interface IServer {

  IMessage postMessage(String senderName, String message);

  IUser followUser(String userName, String followedUserName);

  List<IMessage> getTimeline(String userName);

  List<IMessage> getWall(String userName);
}
