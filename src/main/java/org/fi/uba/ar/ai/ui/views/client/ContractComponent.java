package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBus.SessionEventBus;
import org.vaadin.viritin.button.ConfirmButton;

public class ContractComponent extends CustomComponent {

  private FeedbackForm feedbackForm;

  private Label serviceName = new Label();
  private Label providerName = new Label();
  private Label clientName = new Label();
  private Label scheduledTime = new Label();
  private Label status = new Label();
  private Button done = new ConfirmButton(VaadinIcons.CHECK,
      "Are you sure you want to mark it as done?", this::done);
  private Button send = new Button();

  private User loggedUser;
  private Contract contract;
  private ContractInteractor contractInteractor;
  private EventBus.SessionEventBus eventBus;

  private VerticalLayout root = new VerticalLayout();

  public ContractComponent(User loggedUser, Contract contract,
      ContractInteractor contractInteractor,
      SessionEventBus eventBus, FeedbackForm feedbackForm) {
    this.loggedUser = loggedUser;
    this.contract = contract;
    this.contractInteractor = contractInteractor;
    this.eventBus = eventBus;
    this.feedbackForm = feedbackForm;

    serviceName.setValue(contract.getService().getName());
    serviceName.setCaption("Service");
    User provider = contract.getService().getProvider();
    providerName.setValue(provider.getFirstName() + " " + provider.getLastName());
    providerName.setVisible(!loggedUser.equals(provider));
    providerName.setCaption("Provider");
    User client = contract.getClient();
    clientName.setValue(client.getFirstName() + " " + client.getLastName());
    clientName.setVisible(!loggedUser.equals(client));
    clientName.setCaption("Client");
    scheduledTime.setValue(contract.getScheduledTime().toString());
    scheduledTime.setCaption("Scheduled Time");
    status.setValue(contract.getStatus().name());
    status.setCaption("Status");
    send.setIcon(VaadinIcons.PAPERPLANE_O);
    send.addClickListener(e -> this.send());
    send.setVisible(contract.isCompleted());
    HorizontalLayout horizontalLayout = new HorizontalLayout(providerName, clientName, scheduledTime, status, done, send);
    root.addComponentsAndExpand(horizontalLayout);
    root.setSizeUndefined();
    setCompositionRoot(root);
  }

  private void send() {
    feedbackForm.show(contract, loggedUser);
    feedbackForm.openInModalPopup();
  }

  private void done() {
    try {
      contractInteractor.markAsDoneByUser(contract, loggedUser);
      Notification.show("Success!", Type.HUMANIZED_MESSAGE);
      eventBus.publish(this, new ContractMarkedDoneEvent(contract));
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }
}
