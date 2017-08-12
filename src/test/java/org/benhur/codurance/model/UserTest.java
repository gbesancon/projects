package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;

import org.benhur.codurance.data.User;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class UserTest {

  @Test
  public void testCreation() {
    User user = new User("user");
    assertThat(user.getName(), IsEqual.equalTo("user"));
    assertThat(user.getFollowees(), IsEmptyCollection.empty());
  }

  @Test
  public void testFollowees() {
    User user = new User("user");
    User followee = new User("followee");
    assertThat(user.getFollowees(), IsEmptyCollection.empty());
    user.addFollowee(followee);
    assertThat(user.getFollowees().size(), IsEqual.equalTo(1));
    assertThat(user.getFollowees().get(0), IsEqual.equalTo(followee));
    assertThat(user.getFollowees().get(0).getName(), IsEqual.equalTo("followee"));
  }
}
