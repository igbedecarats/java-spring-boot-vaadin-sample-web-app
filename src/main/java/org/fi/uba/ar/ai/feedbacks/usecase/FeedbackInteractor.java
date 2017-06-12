package org.fi.uba.ar.ai.feedbacks.usecase;

import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;

public class FeedbackInteractor {

  private FeedbackRepository feedbackRepository;

  public FeedbackInteractor(FeedbackRepository feedbackRepository) {
    this.feedbackRepository = feedbackRepository;
  }
}
