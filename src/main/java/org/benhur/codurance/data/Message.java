// Copyright (C) 2017 GBesancon

package org.benhur.codurance.data;

import java.util.Date;

public class Message implements IMessage {

  private final long id;
  private final IUser sender;
  private final Date timestamp;
  private final String text;

  public Message(long id, IUser sender, Date timestamp, String text) {
    this.id = id;
    this.sender = sender;
    this.timestamp = timestamp;
    this.text = text;
  }

  @Override
  public long getId() {
    return id;
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
