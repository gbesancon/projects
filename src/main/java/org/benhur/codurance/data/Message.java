package org.benhur.codurance.data;

import java.util.Date;

public class Message implements IMessage {

  private final IUser sender;
  private final Date timestamp;
  private final String text;

  public Message(IUser sender, Date timestamp, String text) {
    this.sender = sender;
    this.timestamp = timestamp;
    this.text = text;
  }

  @Override
  public IUser getSender() {
    return sender;
  }

  @Override
  public Date getTimestamp() {
    return timestamp;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return timestamp.toString() + ": " + sender.getName() + " -> " + text;
  }
}
