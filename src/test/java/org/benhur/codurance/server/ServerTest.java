// Copyright (C) 2017 GBesancon

package org.benhur.codurance.server;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.benhur.codurance.data.IMessage;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class ServerTest {

  @Test
  public void testEmptyTimeline() {
    IServer server = new Server();
    assertThat(server.getTimeline("sender"), IsEmptyCollection.empty());
  }

  @Test
  public void testPostMessage() {
    IServer server = new Server();
    assertThat(server.getTimeline("sender"), IsEmptyCollection.empty());
    IMessage message1 = server.postMessage("sender", "message1");
    List<IMessage> timeline = server.getTimeline("sender");
    assertThat(timeline.size(), IsEqual.equalTo(1));
    assertThat(timeline.get(0), IsEqual.equalTo(message1));
    assertThat(timeline.get(0).getSender().getName(), IsEqual.equalTo("sender"));
    assertThat(timeline.get(0).getText(), IsEqual.equalTo("message1"));
    IMessage message2 = server.postMessage("sender", "message2");
    timeline = server.getTimeline("sender");
    assertThat(timeline.size(), IsEqual.equalTo(2));
    assertThat(timeline.get(0), IsEqual.equalTo(message2));
    assertThat(timeline.get(0).getSender().getName(), IsEqual.equalTo("sender"));
    assertThat(timeline.get(0).getText(), IsEqual.equalTo("message2"));
    assertThat(timeline.get(1), IsEqual.equalTo(message1));
    assertThat(timeline.get(1).getSender().getName(), IsEqual.equalTo("sender"));
    assertThat(timeline.get(1).getText(), IsEqual.equalTo("message1"));
  }

  @Test
  public void testEmptyWall() {
    IServer server = new Server();
    assertThat(server.getWall("sender"), IsEmptyCollection.empty());
  }

  @Test
  public void testFollowUser() {
    IServer server = new Server();
    assertThat(server.getTimeline("sender1"), IsEmptyCollection.empty());
    assertThat(server.getWall("sender1"), IsEmptyCollection.empty());

    IMessage message1 = server.postMessage("sender1", "message1");
    IMessage message2 = server.postMessage("sender2", "message2");
    IMessage message3 = server.postMessage("sender1", "message3");

    assertThat(server.getTimeline("sender1").size(), IsEqual.equalTo(2));
    assertThat(server.getTimeline("sender2").size(), IsEqual.equalTo(1));
    assertThat(server.getWall("sender1").size(), IsEqual.equalTo(2));
    assertThat(server.getWall("sender2").size(), IsEqual.equalTo(1));

    server.followUser("sender1", "sender2");
    assertThat(server.getTimeline("sender1").size(), IsEqual.equalTo(2));
    assertThat(server.getTimeline("sender2").size(), IsEqual.equalTo(1));
    assertThat(server.getWall("sender1").size(), IsEqual.equalTo(3));
    assertThat(server.getWall("sender2").size(), IsEqual.equalTo(1));

    assertThat(server.getWall("sender1").get(0), IsEqual.equalTo(message3));
    assertThat(server.getWall("sender1").get(0).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(server.getWall("sender1").get(0).getText(), IsEqual.equalTo("message3"));
    assertThat(server.getWall("sender1").get(1), IsEqual.equalTo(message2));
    assertThat(server.getWall("sender1").get(1).getSender().getName(), IsEqual.equalTo("sender2"));
    assertThat(server.getWall("sender1").get(1).getText(), IsEqual.equalTo("message2"));
    assertThat(server.getWall("sender1").get(2), IsEqual.equalTo(message1));
    assertThat(server.getWall("sender1").get(2).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(server.getWall("sender1").get(2).getText(), IsEqual.equalTo("message1"));
  }
}
