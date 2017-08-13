package org.benhur.codurance.client;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
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
        System.out.println(timeline);
      }
    } else if (commandLine.matches(postMessagePattern)) {
      Pattern pattern = Pattern.compile(postMessagePattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String senderName = matcher.group(1);
        String message = matcher.group(2);
        IMessage myMessage = server.postMessage(senderName, message);
        System.out.println(myMessage);
      }
    } else if (commandLine.matches(wallPattern)) {
      Pattern pattern = Pattern.compile(wallPattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String userName = matcher.group(1);
        List<IMessage> wall = server.getWall(userName);
        System.out.println(wall);
      }
    } else if (commandLine.matches(followPattern)) {
      Pattern pattern = Pattern.compile(followPattern);
      Matcher matcher = pattern.matcher(commandLine);
      if (matcher.find()) {
        String userName = matcher.group(1);
        String followeeName = matcher.group(2);
        IUser user = server.followUser(userName, followeeName);
        System.out.println(user);
      }
    }
  }
}
