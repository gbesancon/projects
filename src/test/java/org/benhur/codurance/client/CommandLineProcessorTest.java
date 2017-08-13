package org.benhur.codurance.client;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
import org.benhur.codurance.server.IServer;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineProcessorTest {
  @Test
    public void testPostMessage() throws Exception {
      CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
      commandLineProcessor.processCommandLine(
          "Alice -> I love the weather today",
          new IServer() {
  
            @Override
            public IMessage postMessage(String senderName, String message) {
              assertThat(senderName, IsEqual.equalTo("Alice"));
              assertThat(message, IsEqual.equalTo("I love the weather today"));
              return null;
            }
  
            @Override
            public List<IMessage> getWall(String userName) {
              Assert.fail();
              return null;
            }
  
            @Override
            public List<IMessage> getTimeline(String userName) {
              Assert.fail();
              return null;
            }
  
            @Override
            public IUser followUser(String userName, String followedUserName) {
              Assert.fail();
              return null;
            }
          });
    }

@Test
    public void testTimeline() throws Exception {
      CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
      commandLineProcessor.processCommandLine(
          "Alice",
          new IServer() {
  
            @Override
            public IMessage postMessage(String senderName, String message) {
              Assert.fail();
              return null;
            }
  
            @Override
            public List<IMessage> getWall(String userName) {
              Assert.fail();
              return null;
            }
  
            @Override
            public List<IMessage> getTimeline(String userName) {
              assertThat(userName, IsEqual.equalTo("Alice"));
              return null;
            }
  
            @Override
            public IUser followUser(String userName, String followedUserName) {
              Assert.fail();
              return null;
            }
          });
    }

@Test
  public void testWall() throws Exception {
    CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
    commandLineProcessor.processCommandLine(
        "Alice wall",
        new IServer() {

          @Override
          public IMessage postMessage(String senderName, String message) {
            Assert.fail();
            return null;
          }

          @Override
          public List<IMessage> getWall(String userName) {
            assertThat(userName, IsEqual.equalTo("Alice"));
            return null;
          }

          @Override
          public List<IMessage> getTimeline(String userName) {
            Assert.fail();
            return null;
          }

          @Override
          public IUser followUser(String userName, String followedUserName) {
            Assert.fail();
            return null;
          }
        });
  }

@Test
  public void testFollows() throws Exception {
    CommandLineProcessor commandLineProcessor = new CommandLineProcessor();
    commandLineProcessor.processCommandLine(
        "Alice follows Bob",
        new IServer() {

          @Override
          public IMessage postMessage(String senderName, String message) {
            Assert.fail();
            return null;
          }

          @Override
          public List<IMessage> getWall(String userName) {
            Assert.fail();
            return null;
          }

          @Override
          public List<IMessage> getTimeline(String userName) {
            Assert.fail();
            return null;
          }

          @Override
          public IUser followUser(String userName, String followedUserName) {
            assertThat(userName, IsEqual.equalTo("Alice"));
            assertThat(followedUserName, IsEqual.equalTo("Bob"));
            return null;
          }
        });
  }
}
