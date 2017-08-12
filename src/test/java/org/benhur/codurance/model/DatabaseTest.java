package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;

import org.benhur.codurance.data.Database;
import org.benhur.codurance.data.IDatabase;
import org.benhur.codurance.data.IMessage;
import org.benhur.codurance.data.IUser;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class DatabaseTest {

  @Test
  public void testCreation() {
    IDatabase database = new Database();

    assertThat(database.getUsers(), IsEmptyCollection.empty());
    assertThat(database.getMessages(), IsEmptyCollection.empty());
  }

  @Test
  public void testUserCreate() {
    IDatabase database = new Database();
    assertThat(database.getUsers(), IsEmptyCollection.empty());
    IUser user = database.getOrCreateUser("user");

    assertThat(database.getUsers().size(), IsEqual.equalTo(1));
    assertThat(database.getOrCreateUser("user"), IsEqual.equalTo(user));
    assertThat(database.getUsers().size(), IsEqual.equalTo(1));
  }

  @Test
  public void testMessageCreate() {
    IDatabase database = new Database();
    assertThat(database.getUsers(), IsEmptyCollection.empty());
    assertThat(database.getMessages(), IsEmptyCollection.empty());
    IMessage message = database.addMessage("sender", "message");

    // Test users
    assertThat(database.getUsers().size(), IsEqual.equalTo(1));
    assertThat(database.getUsers().get(0).getName(), IsEqual.equalTo("sender"));

    // Test messages
    assertThat(database.getMessages().size(), IsEqual.equalTo(1));
    assertThat(database.getMessages().get(0), IsEqual.equalTo(message));
    assertThat(database.getMessages().get(0).getSender().getName(), IsEqual.equalTo("sender"));
    assertThat(database.getMessages().get(0).getText(), IsEqual.equalTo("message"));

    // Test messages for user
    assertThat(database.getMessages("sender").size(), IsEqual.equalTo(1));
    assertThat(database.getMessages("sender").get(0), IsEqual.equalTo(message));
    assertThat(
        database.getMessages("sender").get(0).getSender().getName(), IsEqual.equalTo("sender"));
    assertThat(database.getMessages("sender").get(0).getText(), IsEqual.equalTo("message"));
  }

  @Test
  public void testMultiMessageUseCase() {
    IDatabase database = new Database();
    assertThat(database.getUsers(), IsEmptyCollection.empty());
    assertThat(database.getMessages(), IsEmptyCollection.empty());
    IMessage message1 = database.addMessage("sender1", "message1");
    IMessage message2 = database.addMessage("sender2", "message2");
    IMessage message3 = database.addMessage("sender1", "message3");

    // Test users
    assertThat(database.getUsers().size(), IsEqual.equalTo(2));
    assertThat(database.getUsers().get(0).getName(), IsEqual.equalTo("sender1"));
    assertThat(database.getUsers().get(1).getName(), IsEqual.equalTo("sender2"));

    // Test messages
    assertThat(database.getMessages().size(), IsEqual.equalTo(3));
    assertThat(database.getMessages().get(0), IsEqual.equalTo(message1));
    assertThat(database.getMessages().get(0).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(database.getMessages().get(0).getText(), IsEqual.equalTo("message1"));
    assertThat(database.getMessages().get(1), IsEqual.equalTo(message2));
    assertThat(database.getMessages().get(1).getSender().getName(), IsEqual.equalTo("sender2"));
    assertThat(database.getMessages().get(1).getText(), IsEqual.equalTo("message2"));
    assertThat(database.getMessages().get(2), IsEqual.equalTo(message3));
    assertThat(database.getMessages().get(2).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(database.getMessages().get(2).getText(), IsEqual.equalTo("message3"));

    // Test messages for user
    assertThat(database.getMessages("sender1").size(), IsEqual.equalTo(2));
    assertThat(database.getMessages("sender1").get(0), IsEqual.equalTo(message1));
    assertThat(
        database.getMessages("sender1").get(0).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(database.getMessages("sender1").get(0).getText(), IsEqual.equalTo("message1"));
    assertThat(database.getMessages("sender1").get(1), IsEqual.equalTo(message3));
    assertThat(
        database.getMessages("sender1").get(1).getSender().getName(), IsEqual.equalTo("sender1"));
    assertThat(database.getMessages("sender1").get(1).getText(), IsEqual.equalTo("message3"));
    assertThat(database.getMessages("sender2").size(), IsEqual.equalTo(1));
    assertThat(database.getMessages("sender2").get(0), IsEqual.equalTo(message2));
    assertThat(
        database.getMessages("sender2").get(0).getSender().getName(), IsEqual.equalTo("sender2"));
    assertThat(database.getMessages("sender2").get(0).getText(), IsEqual.equalTo("message2"));
  }
}
