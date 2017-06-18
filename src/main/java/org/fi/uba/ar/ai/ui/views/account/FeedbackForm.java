package org.fi.uba.ar.ai.ui.views.account;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import org.apache.commons.lang3.StringUtils;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;
import org.fi.uba.ar.ai.feedbacks.usecase.FeedbackInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class FeedbackForm extends AbstractForm<Feedback> {

  private Contract contract;
  private User loggedUser;
  private FeedbackInteractor feedbackInteractor;

  private final EventBus.SessionEventBus eventBus;

  private TextArea comment = new TextArea("Comentario");
  private NativeSelect<Integer> ratings = new NativeSelect<>("Calificación");

  @Autowired
  public FeedbackForm(FeedbackInteractor feedbackInteractor, EventBus.SessionEventBus eventBus) {
    super(Feedback.class);
    this.feedbackInteractor = feedbackInteractor;
    this.eventBus = eventBus;
    ratings.setItems(Feedback.allowedRatings());
    ratings.setEmptySelectionAllowed(false);
    setSavedHandler(feedback -> send());
    setResetHandler(feedback -> eventBus.publish(this, new FeedbackSubmittedEvent(feedback)));
    setModalWindowTitle("Enviar Evaluación");
    setSizeUndefined();
  }

  private void send() {
    try {
      Feedback feedback = feedbackInteractor
          .submitByUser(loggedUser, contract, comment.getValue(), ratings
              .getValue());
      // send the event for other parts of the application
      eventBus.publish(this, new FeedbackSubmittedEvent(feedback));
      Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  public void show(Contract contract, User loggedUser) {
    setEntity(new Feedback());
    comment.setValue(StringUtils.EMPTY);
    ratings.setSelectedItem(Feedback.allowedRatings().stream().findFirst().get());
    this.contract = contract;
    this.loggedUser = loggedUser;
  }

  @Override
  protected Component createContent() {
    return new MVerticalLayout(new MFormLayout(comment, ratings).withWidth(""),
        getToolbar()).withWidth("");
  }
}
