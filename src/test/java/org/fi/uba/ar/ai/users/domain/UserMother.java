package org.fi.uba.ar.ai.users.domain;

public class UserMother {

  public static User createJohnDoe() {
    return new User("johndoe", "x27", "john.doe@gmail.com", "John", "Doe");
  }

}
