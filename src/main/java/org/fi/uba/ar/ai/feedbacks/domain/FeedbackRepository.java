package org.fi.uba.ar.ai.feedbacks.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

  List<Feedback> findByContractServiceIdAndRecipientId(long serviceId, long userId);
}
