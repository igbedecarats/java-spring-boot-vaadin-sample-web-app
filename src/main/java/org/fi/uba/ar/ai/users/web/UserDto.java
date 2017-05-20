package org.fi.uba.ar.ai.users.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.users.domain.User;

@AllArgsConstructor
@Getter
public class UserDto {

  private long id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;

  public static UserDto toDto(final User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
        user.getLastName());
  }

}
