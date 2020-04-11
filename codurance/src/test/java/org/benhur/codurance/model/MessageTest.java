// Copyright (C) 2017 GBesancon

package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
import org.benhur.codurance.data.Message;
import org.benhur.codurance.data.User;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;

public class MessageTest {

  @Test
  public void testCreation() {
    IUser sender = new User("user");
    IMessage message = new Message(0l, sender, new Date(), "Hello world !");
    assertThat(message.getId(), IsEqual.equalTo(0l));
    assertThat(message.getSender(), IsEqual.equalTo(sender));
    assertThat(message.getTimestamp(), IsNull.notNullValue());
    assertThat(message.getText(), IsEqual.equalTo("Hello world !"));
  }

  @Test
  public void testMultiCreation() {
    IUser sender = new User("user");
    IMessage message1 = new Message(0l, sender, new Date(), "Hello world !");
    IMessage message2 = new Message(1l, sender, new Date(), "Hello world !");
    assertThat(message1.getId(), IsNot.not(IsEqual.equalTo(message2.getSender())));
    assertThat(message1.getSender(), IsEqual.equalTo(message2.getSender()));
    assertThat(
        message1.getTimestamp().getTime(), IsEqual.equalTo(message2.getTimestamp().getTime()));
    assertThat(message1.getText(), IsEqual.equalTo(message2.getText()));
  }
}
