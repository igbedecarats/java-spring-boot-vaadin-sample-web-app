package org.fi.uba.ar.ai.feedbacks;

import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;
import org.fi.uba.ar.ai.feedbacks.usecase.FeedbackInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedbackConfig {

  @Autowired
  private FeedbackRepository feedbackRepository;

  @Bean
  public FeedbackInteractor feedbackInteractor() {
    return new FeedbackInteractor(feedbackRepository);
  }

}
