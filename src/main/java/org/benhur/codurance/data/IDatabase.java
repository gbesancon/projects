package org.benhur.codurance.data;

import java.util.List;

public interface IDatabase {

  List<IUser> getUsers();

  IUser getOrCreateUser(String name);

  List<IMessage> getMessages();

  List<IMessage> getMessages(String userName);

  IMessage addMessage(String senderName, String text);
}
