package org.benhur.codurance.client;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.server.IServer;

public class CommandLineProcessor {

  public void processCommandLine(String commandLine, IServer server) throws Exception {
    String exitPattern = "^exit$";
    String timelinePattern = "^(\\w+)$";
    String postMessagePattern = "^(\\w+) -> (.+)$";
    String wallPattern = "^(\\w+) wall$";
    String followPattern = "^(\\w+) follows (\\w+)$";
    if (commandLine.matches(exitPattern)) {
      throw new Exception("Exit");
    } else if (commandLine.matches(timelinePattern)) {
      Pattern pattern = Pattern.compile(timelinePattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String userName = matcher.group(1);
        List<IMessage> timeline = server.getTimeline(userName);
        printMessages(timeline);
      }
    } else if (commandLine.matches(postMessagePattern)) {
      Pattern pattern = Pattern.compile(postMessagePattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String senderName = matcher.group(1);
        String message = matcher.group(2);
        server.postMessage(senderName, message);
      }
    } else if (commandLine.matches(wallPattern)) {
      Pattern pattern = Pattern.compile(wallPattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String userName = matcher.group(1);
        List<IMessage> wall = server.getWall(userName);
        printMessages(wall);
      }
    } else if (commandLine.matches(followPattern)) {
      Pattern pattern = Pattern.compile(followPattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String userName = matcher.group(1);
        String followeeName = matcher.group(2);
        server.followUser(userName, followeeName);
      }
    }
  }

  private void printMessages(List<IMessage> messages) {
    Date reference = new Date();
    messages.forEach(message -> printMessage(message, reference));
  }

  private void printMessage(IMessage message, Date reference) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(message.getSender().getName());
    stringBuilder.append(" - ");
    stringBuilder.append(message.getText());
    stringBuilder.append(" (");
    stringBuilder.append(printAgeFromReference(reference, message.getTimestamp()));
    stringBuilder.append(")");
    System.out.println(stringBuilder.toString());
  }

  private String printAgeFromReference(Date reference, Date date) {
    StringBuilder stringBuilder = new StringBuilder();
    long deltaInMilliseconds = reference.getTime() - date.getTime();
    long deltaInSeconds = deltaInMilliseconds / 1000;
    long deltaInMinutes = deltaInSeconds / 60;
    if (deltaInMinutes != 0) {
      stringBuilder.append(deltaInMinutes);
      stringBuilder.append(deltaInMinutes == 1 ? " minute ago" : " minutes ago");
    } else if (deltaInSeconds != 0) {
      stringBuilder.append(deltaInSeconds);
      stringBuilder.append(deltaInSeconds == 1 ? " second ago" : " seconds ago");
    }
    return stringBuilder.toString();
  }
}
