package org.benhur.codurance.client;

import org.benhur.codurance.server.IServer;

public class Client {
  private final IServer server;

  public Client(IServer server) {
    this.server = server;
  }
}
