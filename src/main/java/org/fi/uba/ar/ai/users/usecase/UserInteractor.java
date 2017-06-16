package org.fi.uba.ar.ai.users.usecase;

import java.util.List;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;
import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;
import org.fi.uba.ar.ai.users.domain.RatedUser;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRepository;

public class UserInteractor {

  private UserRepository userRepository;

  private FeedbackRepository feedbackRepository;

  public UserInteractor(UserRepository userRepository,
      FeedbackRepository feedbackRepository) {
    this.userRepository = userRepository;
    this.feedbackRepository = feedbackRepository;
  }

  public List<User> findAll() {
    return (List<User>) userRepository.findAll();
  }

  public void delete(User user) {
    userRepository.delete(user);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

  public RatedUser calculateUserRating(final User user) {
    List<Feedback> feedbacks = feedbackRepository.findByRecipientId(user.getId());
    float rating = 0f;
    for (Feedback feedback : feedbacks) {
      rating += feedback.getRating();
    }
    if (feedbacks.size() > 0) {
      rating = rating / feedbacks.size();
    }
    return new RatedUser(user, rating);
  }
}
