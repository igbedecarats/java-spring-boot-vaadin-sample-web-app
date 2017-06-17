package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_PROVIDER")
@SpringView(name = "myservices")
@SideBarItem(sectionId = Sections.SERVICES, caption = "My Services", order = 0)
@FontAwesomeIcon(FontAwesome.COGS)
public class MyServicesView extends CustomComponent implements View {

  private User loggedUser;

  private ServiceInteractor serviceInteractor;

  private final EventBus.SessionEventBus eventBus;

  private ServiceForm form;

  private VerticalLayout servicesContainer = new VerticalLayout();


  @Autowired
  public MyServicesView(ServiceForm form, EventBus.SessionEventBus eventBus,
      ServiceInteractor serviceInteractor) {

    this.serviceInteractor = serviceInteractor;
    this.eventBus = eventBus;
    this.eventBus.subscribe(this);
    this.form = form;
    loggedUser = SpringContextUserHolder.getUser();

    VerticalLayout searchAndAddLayout = new VerticalLayout();
    HorizontalLayout hl = new HorizontalLayout();
    hl.setSizeUndefined();
    TextField searchTextField = new TextField();
    searchTextField.setPlaceholder("Search by Name");
    searchTextField.addValueChangeListener(e -> search(searchTextField.getValue()));
    searchTextField.setWidth("600px");
    Button add = new Button();
    add.setIcon(VaadinIcons.PLUS);
    add.addClickListener(e -> add());
    hl.addComponentsAndExpand(searchTextField, add);
    searchAndAddLayout.addComponents(hl);
    searchAndAddLayout.setSizeUndefined();
    searchAndAddLayout.setComponentAlignment(hl, Alignment.TOP_CENTER);
    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.addComponent(searchAndAddLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(searchAndAddLayout, Alignment.TOP_CENTER);

    Panel panel = new Panel("Services");
    panel.setWidth("1000px");
    panel.setHeight("450px");
    servicesContainer.setSizeUndefined();
    panel.setContent(servicesContainer);
    rootLayout.addComponent(panel);
    setCompositionRoot(rootLayout);
    search("");
  }

  private void add() {
    form.setService(new Service());
    form.openInModalPopup();
  }

  private void search(String serviceName) {
    servicesContainer.removeAllComponents();
    List<Service> services = serviceInteractor
        .findAllByProviderMatchingName(loggedUser, serviceName);
    populateList(services);
  }

  private void populateList(List<Service> services) {
    services.stream().forEach(service -> {
      MyServiceComponent myServiceComponent = new MyServiceComponent(service, loggedUser,
          serviceInteractor, form, this);
      servicesContainer.addComponent(myServiceComponent);
    });
  }

  public void updateList() {
    servicesContainer.removeAllComponents();
    List<Service> services = serviceInteractor.findAllByProviderId(loggedUser.getId());
    populateList(services);
  }

  @EventBusListenerMethod(scope = EventScope.SESSION)
  public void onServiceCreatedEvent(ServiceCreatedEvent event) {
    search("");
    form.closePopup();
  }

  @Override
  public void enter(ViewChangeEvent event) {
  }
}
