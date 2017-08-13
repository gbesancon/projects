package org.benhur.codurance.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.benhur.codurance.server.IServer;

public class Client {
  private final IServer server;

  public Client(IServer server) {
    this.server = server;
  }

  public void start() {
    printInstructions();
    BufferedReader inputBufferReader = new BufferedReader(new InputStreamReader(System.in));

    try {
      CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
      while (true) {
        System.out.print("> ");
        String commandLine = inputBufferReader.readLine();
        commandLineProcessor.processCommandLine(commandLine, server);
      }
    } catch (Exception e) {
      System.err.println(e.getMessage());
      return;
    }
  }

  private void printInstructions() {
    System.out.println("Enter command, or 'exit' to quit");
  }
}
