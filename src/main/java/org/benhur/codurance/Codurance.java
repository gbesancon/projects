package org.benhur.codurance;

import org.benhur.codurance.client.Client;
import org.benhur.codurance.server.IServer;
import org.benhur.codurance.server.Server;

public class Codurance {

  public static void main(String[] args) {
    IServer server = new Server();
    Client client = new Client(server);
    client.start();
  }
}
