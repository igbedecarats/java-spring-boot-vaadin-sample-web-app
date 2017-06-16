package org.fi.uba.ar.ai.ui.views.account;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "contracts")
@SideBarItem(sectionId = Sections.ACCOUNT, caption = "Contracts", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class ContractsView extends CustomComponent implements View {

  private ContractInteractor contractInteractor;

  private VerticalLayout contractContainer = new VerticalLayout();

  private User loggedUser;

  private final EventBus.SessionEventBus eventBus;

  private FeedbackForm feedbackForm;

  @Autowired
  public ContractsView(ContractInteractor contractInteractor, EventBus.SessionEventBus eventBus,
      FeedbackForm feedbackForm) {
    this.contractInteractor = contractInteractor;
    this.eventBus = eventBus;
    this.eventBus.subscribe(this);
    this.feedbackForm = feedbackForm;
    loggedUser = SpringContextUserHolder.getUser();
    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.setSizeFull();
    Panel panel = new Panel("Contracts");
    panel.setWidth("1000px");
    panel.setHeight("600px");
    contractContainer.setSizeUndefined();
    panel.setContent(contractContainer);
    rootLayout.addComponent(panel);
    setCompositionRoot(rootLayout);
    updateList();
  }

  public void updateList() {
    List<Contract> contracts = contractInteractor.findForUser(loggedUser);
    contractContainer.removeAllComponents();
    contracts.stream()
        .forEach(contract -> contractContainer
            .addComponent(new ContractComponent(loggedUser, contract, contractInteractor, eventBus,
                feedbackForm)));
  }

  @EventBusListenerMethod(scope = EventScope.SESSION)
  public void onContractMarkedDoneEvent(ContractMarkedDoneEvent event) {
    updateList();
  }

  @EventBusListenerMethod(scope = EventScope.SESSION)
  public void onFeedbackSubmittedEvent(FeedbackSubmittedEvent event) {
    feedbackForm.closePopup();
    updateList();
  }

  @Override
  public void enter(ViewChangeEvent event) {
  }

}
