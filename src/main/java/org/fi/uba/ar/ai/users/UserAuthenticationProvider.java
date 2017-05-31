package org.fi.uba.ar.ai.users;

import java.util.Arrays;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRepository;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserAuthenticationProvider implements AuthenticationProvider {

  private UserRepository userRepository;

  public UserAuthenticationProvider(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        (UsernamePasswordAuthenticationToken) authentication;
    final String username = (String) usernamePasswordAuthenticationToken.getPrincipal();
    final String password = (String) usernamePasswordAuthenticationToken.getCredentials();
    User user = userRepository.findByUsername(username);
    if (user != null && user.getEncryptedPassword().equals(password)) {
      AbstractAuthenticationToken abstractAuthenticationToken = getAbstractAuthenticationToken(
          user);
      abstractAuthenticationToken.setAuthenticated(true);
      return abstractAuthenticationToken;
    }
    throw new UsernameNotFoundException(username);
  }

  private AbstractAuthenticationToken getAbstractAuthenticationToken(User user) {
    return new AbstractAuthenticationToken(Arrays.asList(user.getRole())) {
      @Override
      public Object getCredentials() {
        return user.getEncryptedPassword();
      }

      @Override
      public Object getPrincipal() {
        return user;
      }
    };
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}
