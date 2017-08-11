package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.Date;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
import org.benhur.codurance.data.Message;
import org.benhur.codurance.data.User;
import org.junit.Test;

public class MessageTest {

  @Test
  public void testCreation() {
    IUser sender = new User("myUser");
    Date timestamp = new Date();
    IMessage message = new Message(sender, timestamp, "Hello world !");
    assertThat(message.getSender(), is(sender));
    assertThat(message.getTimestamp(), is(timestamp));
    assertThat(message.getText(), is("Hello world !"));
  }
}
