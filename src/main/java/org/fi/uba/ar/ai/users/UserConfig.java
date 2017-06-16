package org.fi.uba.ar.ai.users;

import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;
import org.fi.uba.ar.ai.users.domain.UserRepository;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private FeedbackRepository feedbackRepository;

  @Bean
  public UserInteractor userInteractor() {
    return new UserInteractor(userRepository, feedbackRepository);
  }

}
