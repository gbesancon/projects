package org.benhur.codurance.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.benhur.codurance.data.User;
import org.junit.Test;

public class UserTest {

  @Test
  public void testCreation() {
    User user = new User("myUser");
    assertThat(user.getName(), is("myUser"));
  }
}
