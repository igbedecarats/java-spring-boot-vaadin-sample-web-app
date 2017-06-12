package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.ui.Button;
import com.vaadin.ui.DateTimeField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;

public class QuotationForm extends VerticalLayout {

  private Service service;
  private User loggedUser;

  private QuotationInteractor quotationInteractor;

  private VerticalLayout layout = new VerticalLayout();
  private DateTimeField time = new DateTimeField("Scheduled");
  private TextArea description = new TextArea("Considerations");
  private Button cancel = new Button("Cancel");
  private Button send = new Button("Send");

  public QuotationForm(QuotationInteractor quotationInteractor) {
    this.quotationInteractor = quotationInteractor;
    description.setStyleName(ValoTheme.TEXTAREA_LARGE);
    description.setWordWrap(true);
    description.setWidth("100%");
    time.setValue(LocalDateTime.now());
    send.addClickListener(event -> this.send());
    cancel.addClickListener(event -> this.cancel());
    send.setStyleName(ValoTheme.BUTTON_PRIMARY);
    cancel.setStyleName(ValoTheme.BUTTON_QUIET);
    HorizontalLayout buttons = new HorizontalLayout(cancel, send);
    layout.addComponents(description, time, buttons);
    addComponent(layout);
    setSizeFull();
  }

  private void cancel() {
    setVisible(false);
  }

  private void send() {
    quotationInteractor.create(description.getValue(), loggedUser, service, time.getValue());
    Notification.show("Success!", Type.HUMANIZED_MESSAGE);
    setVisible(false);
  }

  public void show(Service service, User loggedUser) {
    description.setValue(StringUtils.EMPTY);
    time.setValue(LocalDateTime.now());
    this.service = service;
    this.loggedUser = loggedUser;
  }
}
