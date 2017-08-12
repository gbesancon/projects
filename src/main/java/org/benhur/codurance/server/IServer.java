package org.benhur.codurance.server;

import java.util.List;
import org.benhur.codurance.data.IMessage;

public interface IServer {

  IMessage postMessage(String senderName, String message);

  void followUser(String userName, String followedUserName);

  List<IMessage> getTimeline(String userName);

  List<IMessage> getWall(String userName);
}
