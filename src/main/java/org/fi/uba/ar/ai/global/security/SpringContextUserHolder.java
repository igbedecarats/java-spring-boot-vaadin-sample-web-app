package org.fi.uba.ar.ai.global.security;

import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringContextUserHolder {

  public static User getUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
