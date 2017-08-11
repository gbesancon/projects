package org.benhur.codurance.server;

import java.util.Collection;
import org.benhur.codurance.model.IMessage;

public class Server implements IServer {

  @Override
  public void postMessage(String user, String message) {
    // TODO Auto-generated method stub

  }

  @Override
  public void followUser(String user, String followedUser) {
    // TODO Auto-generated method stub

  }

  @Override
  public Collection<IMessage> getTimeline(String user) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Collection<IMessage> getWall(String user) {
    // TODO Auto-generated method stub
    return null;
  }
}
