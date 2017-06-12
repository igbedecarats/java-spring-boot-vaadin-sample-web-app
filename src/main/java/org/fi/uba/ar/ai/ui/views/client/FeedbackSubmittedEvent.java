package org.fi.uba.ar.ai.ui.views.client;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;

@Getter
@AllArgsConstructor
public class FeedbackSubmittedEvent implements Serializable {

  private Feedback feedback;
}
