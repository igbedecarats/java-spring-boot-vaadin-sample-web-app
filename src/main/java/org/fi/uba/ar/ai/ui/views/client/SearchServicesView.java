package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Search", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class SearchServicesView extends CustomComponent implements View {

  private User loggedUser;

  private LocationInteractor locationInteractor;

  private ServiceInteractor serviceInteractor;

  private VerticalLayout servicesContainer = new VerticalLayout();

  @Autowired
  public SearchServicesView(LocationInteractor locationInteractor,
      ServiceInteractor serviceInteractor) {
    this.locationInteractor = locationInteractor;
    this.serviceInteractor = serviceInteractor;
    User loggedUser = SpringContextUserHolder.getUser();
    VerticalLayout searchLayout = new VerticalLayout();
    TextField searchTextField = new TextField();
    searchTextField.addValueChangeListener(e -> simpleSearch(searchTextField.getValue()));
    searchTextField.setWidth("600px");
    CheckBox advanceSearch = new CheckBox("Búsqueda avanzada");
    searchLayout.addComponents(searchTextField, advanceSearch);
    searchLayout.setSizeUndefined();
    searchLayout.setComponentAlignment(searchTextField, Alignment.TOP_CENTER);
    searchLayout.setComponentAlignment(advanceSearch, Alignment.TOP_CENTER);
    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.addComponent(searchLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);
    Panel panel = new Panel("Services");
    panel.setContent(servicesContainer);
    panel.setSizeFull();
    servicesContainer.setSizeUndefined();
    rootLayout.addComponent(panel);
    setCompositionRoot(rootLayout);
    setSizeFull();
    simpleSearch("");
  }

  private void simpleSearch(String value) {
    List<Service> services = serviceInteractor.findAll(value);
    servicesContainer.removeAllComponents();
    services.stream()
        .forEach(service -> servicesContainer.addComponent(new ServiceComponent(service)));
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
  }
}
