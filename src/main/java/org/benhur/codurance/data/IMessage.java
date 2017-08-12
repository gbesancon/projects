package org.benhur.codurance.data;

import java.util.Date;

public interface IMessage {

  long getId();

  IUser getSender();

  Date getTimestamp();

  String getText();
}
