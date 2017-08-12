package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
import org.benhur.codurance.data.Message;
import org.benhur.codurance.data.User;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class MessageTest {

  @Test
  public void testCreation() {
    IUser sender = new User("user");
    Date timestamp = new Date();
    IMessage message = new Message(sender, timestamp, "Hello world !");
    assertThat(message.getSender(), IsEqual.equalTo(sender));
    assertThat(message.getTimestamp(), IsEqual.equalTo(timestamp));
    assertThat(message.getText(), IsEqual.equalTo("Hello world !"));
  }
}
