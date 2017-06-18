package org.fi.uba.ar.ai.ui.views.search;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.ui.views.account.QuotationForm;
import org.fi.uba.ar.ai.ui.views.account.QuotationModifiedEvent;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.SEARCH, caption = "Search", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class SearchServicesView extends CustomComponent implements View {

  private User loggedUser;

  private final EventBus.SessionEventBus eventBus;

  private final QuotationForm quotationForm;

  private ServiceInteractor serviceInteractor;

  private LocationInteractor locationInteractor;

  private VerticalLayout servicesContainer = new VerticalLayout();

  @Autowired
  public SearchServicesView(EventBus.SessionEventBus eventBus, QuotationForm quotationForm,
      ServiceInteractor serviceInteractor, LocationInteractor locationInteractor) {
    Validate.notNull(eventBus, "The Event Bus cannot be null");
    Validate.notNull(quotationForm, "The Quotation Form cannot be null");
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null");
    Validate.notNull(locationInteractor, "The Location Interactor cannot be null");
    this.serviceInteractor = serviceInteractor;
    this.eventBus = eventBus;
    this.eventBus.subscribe(this);
    this.quotationForm = quotationForm;
    this.locationInteractor = locationInteractor;
    loggedUser = SpringContextUserHolder.getUser();
    VerticalLayout searchLayout = new VerticalLayout();
    HorizontalLayout simpleSearchLayout = new HorizontalLayout();
    TextField searchTextField = new TextField();
    searchTextField.setPlaceholder("Buscar por Nombre");
    searchTextField.addValueChangeListener(e -> simpleSearch(searchTextField.getValue()));
    searchTextField.setWidth("600px");
    CheckBox advanceSearch = new CheckBox("Búsqueda avanzada");
    simpleSearchLayout.addComponents(searchTextField, advanceSearch);
    simpleSearchLayout.setSizeUndefined();
    simpleSearchLayout.setComponentAlignment(searchTextField, Alignment.TOP_CENTER);
    simpleSearchLayout.setComponentAlignment(advanceSearch, Alignment.TOP_CENTER);
    HorizontalLayout advancedSearchLayout = new HorizontalLayout();
    advancedSearchLayout.setVisible(false);
    advanceSearch.addValueChangeListener(
        event -> advancedSearchLayout.setVisible(advancedSearchLayout.isVisible() ? false : true));
    CheckBoxGroup<String> areas = new CheckBoxGroup<>("Areas",
        Arrays.asList(LocationArea.values()).stream().map(locationArea -> locationArea.getValue())
            .collect(Collectors.toList()));
    CheckBoxGroup<String> categories = new CheckBoxGroup<>("Categorias",
        serviceInteractor.findAllCategories().stream()
            .map(serviceCategory -> serviceCategory.getName()).collect(
            Collectors.toList()));
    NativeSelect<String> startDays = new NativeSelect<>("Dia Desde", Service.getLocalizedDaysOfTheWeek());
    NativeSelect<String> endDays = new NativeSelect<>("Dia Hasta", Service.getLocalizedDaysOfTheWeek());
    advancedSearchLayout.addComponents(areas, categories, startDays, endDays);
    searchLayout.addComponents(simpleSearchLayout, advancedSearchLayout);

    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.addComponent(searchLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);

    Panel panel = new Panel("Servicios");
    panel.setWidth("1000px");
    panel.setHeight("450px");
    servicesContainer.setSizeUndefined();
    panel.setContent(servicesContainer);
    rootLayout.addComponent(panel);
    setCompositionRoot(rootLayout);
    simpleSearch("");
  }

  private void simpleSearch(String value) {
    List<Service> services = serviceInteractor.findAllMatchingName(value).stream()
        .filter(service -> !service.getProvider().equals(loggedUser)).collect(Collectors.toList());
    servicesContainer.removeAllComponents();
    services.stream()
        .forEach(service -> servicesContainer
            .addComponent(new SearchServiceComponent(service, loggedUser, serviceInteractor,
                quotationForm)));
  }

  @EventBusListenerMethod(scope = EventScope.SESSION)
  public void onQuotationModifiedEvent(QuotationModifiedEvent event) {
    simpleSearch("");
    quotationForm.closePopup();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
  }
}
