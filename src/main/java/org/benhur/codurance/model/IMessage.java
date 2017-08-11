package org.benhur.codurance.model;

import java.util.Date;

public interface IMessage {

  IUser getSender();

  Date getTimestamp();

  String getText();
}
