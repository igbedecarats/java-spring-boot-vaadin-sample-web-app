package org.fi.uba.ar.ai.users.domain;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

  CLIENT, PROVIDER, ADMIN;

  @Override
  public String getAuthority() {
    return "ROLE_" + name();
  }

}
