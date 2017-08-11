package org.benhur.codurance.server;

import java.util.Collection;
import org.benhur.codurance.data.IMessage;

public interface IServer {

  void postMessage(String user, String message);

  void followUser(String user, String followedUser);

  Collection<IMessage> getTimeline(String user);

  Collection<IMessage> getWall(String user);
}
