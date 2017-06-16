package org.fi.uba.ar.ai.ui.views.account;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.ValoTheme;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class QuotationForm extends AbstractForm<Quotation> {

  private Service service;
  private User loggedUser;

  private QuotationInteractor quotationInteractor;
  private final EventBus.SessionEventBus eventBus;


  private DateTimeField scheduledTime = new DateTimeField("Scheduled");
  private TextArea description = new TextArea("Considerations");

  @Autowired
  public QuotationForm(QuotationInteractor quotationInteractor, EventBus.SessionEventBus eventBus) {
    super(Quotation.class);
    this.quotationInteractor = quotationInteractor;
    this.eventBus = eventBus;
    description.setStyleName(ValoTheme.TEXTAREA_LARGE);
    description.setWordWrap(true);
    description.setWidth("100%");
    scheduledTime.setValue(LocalDateTime.now());
    setSavedHandler(quotation -> send());
    setResetHandler(quotation -> eventBus.publish(this, new QuotationModifiedEvent(quotation)));
    setModalWindowTitle("Send Quotation");
    setSizeUndefined();
  }

  private void send() {
    try {
      Quotation quotation = quotationInteractor.create(description.getValue(), loggedUser, service, scheduledTime.getValue());
      // send the event for other parts of the application
      eventBus.publish(this, new QuotationModifiedEvent(quotation));
      Notification.show("Success!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }


  public void show(Service service, User loggedUser) {
    setEntity(new Quotation());
    description.setValue(StringUtils.EMPTY);
    scheduledTime.setValue(LocalDateTime.now());
    this.service = service;
    this.loggedUser = loggedUser;
  }

  @Override
  protected Component createContent() {
    return new MVerticalLayout(new MFormLayout(description, scheduledTime).withWidth(""),
        getToolbar()).withWidth("");
  }
}
