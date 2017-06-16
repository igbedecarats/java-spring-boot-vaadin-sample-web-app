package org.fi.uba.ar.ai.users.domain;

import lombok.Getter;

@Getter
public class RatedUser extends User {

  private float rating;

  public RatedUser(final User user, final float rating) {
    super(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(),
        user.getLastName(), user.getRole());
    this.rating = rating;
  }

}
