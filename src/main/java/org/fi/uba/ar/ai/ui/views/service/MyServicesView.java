package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_PROVIDER")
@SpringView(name = "myservices")
@SideBarItem(sectionId = Sections.ADMIN, caption = "My Services")
@FontAwesomeIcon(FontAwesome.COGS)
public class MyServicesView extends CustomComponent implements View {

  private User loggedUser;

  private UserInteractor userInteractor;

  private LocationInteractor locationInteractor;

  private ServiceInteractor serviceInteractor;

  private VerticalLayout rootContainer;

  private HorizontalLayout searchContainer;

  private VerticalLayout servicesContainer;

  private PopupView popUp;

  private ServicePopUpContent popUpContent;

  @Autowired
  public MyServicesView(UserInteractor userInteractor,
      LocationInteractor locationInteractor,
      ServiceInteractor serviceInteractor) {
    this.userInteractor = userInteractor;
    this.locationInteractor = locationInteractor;
    this.serviceInteractor = serviceInteractor;

    rootContainer = new VerticalLayout();
    rootContainer.setSizeUndefined();
    setCompositionRoot(rootContainer);

    searchContainer = new HorizontalLayout();
    searchContainer.setSizeUndefined();
    rootContainer.addComponent(searchContainer);
    TextField searchServiceName = new TextField();
    Button search = new Button();
    search.setIcon(VaadinIcons.SEARCH);
    search.addClickListener(e -> this.search(searchServiceName.getValue()));
    Button add = new Button();
    add.setIcon(VaadinIcons.PLUS);
    add.addClickListener(e -> add());
    searchContainer.addComponents(searchServiceName, search, add);

    servicesContainer = new VerticalLayout();
    servicesContainer.setSizeUndefined();
    Panel panel = new Panel("My Services");
    panel.setContent(servicesContainer);
    panel.setWidth("1000px");
    panel.setHeight("550px");
    rootContainer.addComponent(panel);

    loggedUser = SpringContextUserHolder.getUser();

    popUpContent = new ServicePopUpContent(loggedUser, this.serviceInteractor, this.locationInteractor, this);
    popUp = new PopupView(popUpContent);
    popUp.setHideOnMouseOut(false);
    rootContainer.addComponent(popUp);

    updateList();
  }

  private void add() {
    popUpContent.setService(new Service());
    popUp.setPopupVisible(true);
  }

  private void search(String serviceName) {
    servicesContainer.removeAllComponents();
    List<Service> services = serviceInteractor.findWithNameLike(serviceName);
    populateList(services);
  }

  private void populateList(List<Service> services) {
    services.stream().forEach(service -> servicesContainer
        .addComponent(new ServiceComponent(service, serviceInteractor, popUp, popUpContent, this)));
  }

  public void updateList() {
    servicesContainer.removeAllComponents();
    List<Service> services = serviceInteractor.findAll();
    populateList(services);
  }

  public void closePopUp() {
    popUp.setPopupVisible(false);
    updateList();
  }

  @Override
  public void enter(ViewChangeEvent event) {
  }
}
