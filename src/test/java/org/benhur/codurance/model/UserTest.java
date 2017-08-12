package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;

import org.benhur.codurance.data.User;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class UserTest {

  @Test
  public void testCreation() {
    User user = new User("user");
    assertThat(user.getName(), IsEqual.equalTo("user"));
  }
}
