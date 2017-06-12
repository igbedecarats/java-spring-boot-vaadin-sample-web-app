package org.fi.uba.ar.ai.feedbacks.usecase;

import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.domain.ContractRepository;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;
import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;
import org.fi.uba.ar.ai.users.domain.User;

public class FeedbackInteractor {

  private FeedbackRepository feedbackRepository;

  private ContractRepository contractRepository;

  public FeedbackInteractor(FeedbackRepository feedbackRepository,
      ContractRepository contractRepository) {
    this.feedbackRepository = feedbackRepository;
    this.contractRepository = contractRepository;
  }

  public Feedback submitByUser(User user, Contract contract, String comment, Integer rating) {
    User sender = user;
    User recipient;
    if (user.equals(contract.getClient())) {
      recipient = contract.getService().getProvider();
    } else if (user.equals(contract.getService().getProvider())) {
      recipient = contract.getClient();
    } else {
      throw new IllegalArgumentException("The Users must belong to the Contract");
    }
    Feedback feedback = feedbackRepository.save(new Feedback(sender,recipient, contract, rating, comment));
    contract.addFeedback(feedback);
    contractRepository.save(contract);
    return feedback;
  }
}
