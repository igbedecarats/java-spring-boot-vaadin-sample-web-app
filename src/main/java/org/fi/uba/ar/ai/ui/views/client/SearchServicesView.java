package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
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

  private Grid<Location> grid = new Grid<>();

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
    setCompositionRoot(rootLayout);
    setSizeFull();
  }

  private void simpleSearch(String value) {
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
  }
}
