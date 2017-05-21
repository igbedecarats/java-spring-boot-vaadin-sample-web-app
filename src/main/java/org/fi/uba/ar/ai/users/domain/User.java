package org.fi.uba.ar.ai.users.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fi.uba.ar.ai.locations.domain.Location;

@Entity
@Table(name = "user")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class User {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "encrypted_password", nullable = false)
  @Setter
  private String encryptedPassword;

  @Column(name = "email", nullable = false, unique = true)
  @Setter
  private String email;

  @Column(name = "first_name", nullable = false)
  @Setter
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @ManyToOne
  @JoinColumn(name = "preferred_location_id")
  @Setter
  private Location location;

  public User(String username, String encryptedPassword, String email, String firstName,
      String lastName) {
    this.username = username;
    this.encryptedPassword = encryptedPassword;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return new EqualsBuilder().append(id, user.id).append(username, user.getUsername())
        .append(email, user.getEmail()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(username).append(email).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("username", username)
        .append("email", email)
        .append("firstName", firstName).append("lastName", lastName).toString();
  }
}
