package org.fi.uba.ar.ai.users;

import org.fi.uba.ar.ai.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;

@Configuration
public class SecurityConfiguration  implements AuthenticationManagerConfigurer {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth.inMemoryAuthentication()
//        .withUser("user").password("user").roles("USER")
//        .and()
//        .withUser("admin").password("admin").roles("ADMIN");
    auth.authenticationProvider(new UserAuthenticationProvider(userRepository));
  }
}
