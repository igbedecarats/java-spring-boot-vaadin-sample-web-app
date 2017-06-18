package org.fi.uba.ar.ai.ui.views.search;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CheckBoxGroup;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
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

  private VerticalLayout servicesContainer = new VerticalLayout();

  private TextField searchTextField;
  private CheckBox advanceSearch;
  private CheckBox nearBy;
  private CheckBoxGroup<String> areas;
  private CheckBoxGroup<String> categories;
  private NativeSelect<String> startDays;
  private NativeSelect<String> endDays;

  private List<String> existingAreas;
  private List<String> existingCategories;

  @Autowired
  public SearchServicesView(EventBus.SessionEventBus eventBus, QuotationForm quotationForm,
      ServiceInteractor serviceInteractor) {
    Validate.notNull(eventBus, "The Event Bus cannot be null");
    Validate.notNull(quotationForm, "The Quotation Form cannot be null");
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null");
    this.serviceInteractor = serviceInteractor;
    this.eventBus = eventBus;
    this.eventBus.subscribe(this);
    this.quotationForm = quotationForm;
    loggedUser = SpringContextUserHolder.getUser();
    VerticalLayout searchLayout = new VerticalLayout();
    HorizontalLayout simpleSearchLayout = new HorizontalLayout();
    searchTextField = new TextField();
    searchTextField.setPlaceholder("Buscar por Nombre");
    searchTextField.addValueChangeListener(e -> simpleSearch(searchTextField.getValue()));
    searchTextField.setWidth("600px");
    searchTextField.setEnabled(true);
    nearBy = new CheckBox("Cerca tuyo");
    nearBy.setEnabled(true);
    nearBy.addValueChangeListener(event -> simpleSearch(
        StringUtils.isNotBlank(searchTextField.getValue()) ? searchTextField.getValue()
            : StringUtils.EMPTY));
    advanceSearch = new CheckBox("Búsqueda avanzada");
    simpleSearchLayout.addComponents(searchTextField, nearBy, advanceSearch);
    simpleSearchLayout.setSizeUndefined();
    simpleSearchLayout.setComponentAlignment(searchTextField, Alignment.TOP_CENTER);
    simpleSearchLayout.setComponentAlignment(advanceSearch, Alignment.TOP_CENTER);
    HorizontalLayout advancedSearchLayout = new HorizontalLayout();
    advancedSearchLayout.setVisible(false);
    advanceSearch.addValueChangeListener(
        event -> {
          advancedSearchLayout.setVisible(advancedSearchLayout.isVisible() ? false : true);
          searchTextField.setEnabled(searchTextField.isEnabled() ? false : true);
          nearBy.setEnabled(nearBy.isEnabled() ? false : true);
        });
    existingAreas = Arrays.asList(LocationArea.values()).stream()
        .map(locationArea -> locationArea.getValue())
        .collect(Collectors.toList());
    areas = new CheckBoxGroup<>("Areas", existingAreas);
    existingCategories = serviceInteractor.findAllCategories().stream()
        .map(serviceCategory -> serviceCategory.getName()).collect(
            Collectors.toList());
    categories = new CheckBoxGroup<>("Categorias", existingCategories);
    startDays = new NativeSelect<>("Dia Desde", Service.getLocalizedDaysOfTheWeek());
    endDays = new NativeSelect<>("Dia Hasta", Service.getLocalizedDaysOfTheWeek());
    Button advanceSearch = new Button();
    advanceSearch.setIcon(VaadinIcons.SEARCH);
    advanceSearch.addClickListener(event -> this.advanceSearch());
    VerticalLayout advanceSearchButtonVerticalLayout = new VerticalLayout(advanceSearch);
    advanceSearchButtonVerticalLayout.setComponentAlignment(advanceSearch, Alignment.MIDDLE_CENTER);
    advancedSearchLayout
        .addComponents(areas, categories, startDays, endDays, advanceSearchButtonVerticalLayout);
    searchLayout.addComponents(simpleSearchLayout, advancedSearchLayout);

    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.addComponent(searchLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);

    Panel panel = new Panel("Servicios");
    panel.setWidth("1000px");
    panel.setHeight("380px");
    servicesContainer.setSizeUndefined();
    panel.setContent(servicesContainer);
    rootLayout.addComponent(panel);
    setCompositionRoot(rootLayout);
    simpleSearch("");
  }

  private void advanceSearch() {
    if (advanceSearch.getValue()) {
      servicesContainer.removeAllComponents();
      List<LocationArea> searchAreas;
      if (this.areas.getSelectedItems().isEmpty()) {
        searchAreas = Arrays.asList(LocationArea.values());
      } else {
        searchAreas = new ArrayList<>(this.areas.getSelectedItems().stream()
            .map(area -> LocationArea.getByValue(area)).collect(
                Collectors.toList()));
      }
      List<String> searchCategories;
      if (this.categories.getSelectedItems().isEmpty()) {
        searchCategories = existingCategories;
      } else {
        searchCategories = new ArrayList<>(this.categories.getSelectedItems());
      }
      Integer searchStartDay;
      if (startDays.isEmpty()) {
        searchStartDay = DayOfWeek.of(1).getValue();
      } else {
        searchStartDay = Service.getDayOfTheWeekFromLocalizedDay(startDays.getSelectedItem().get());
      }
      Integer searchEndDays;
      if (endDays.isEmpty()) {
        searchEndDays = DayOfWeek.of(7).getValue();
      } else {
        searchEndDays = Service.getDayOfTheWeekFromLocalizedDay(endDays.getSelectedItem().get());
      }
      List<Service> services = serviceInteractor
          .searchBy(StringUtils.EMPTY, searchAreas, searchCategories, searchStartDay,
              searchEndDays);
      populateServicesContainer(services);
    }
  }

  private void simpleSearch(String value) {
    List<Service> services = removeLoggedUserFromServiceList(
        serviceInteractor.findAllMatchingName(value, loggedUser, nearBy.getValue()));
    populateServicesContainer(services);
  }

  private List<Service> removeLoggedUserFromServiceList(List<Service> services) {
    return services.stream()
        .filter(service -> !service.getProvider().equals(loggedUser)).collect(Collectors.toList());
  }

  private void populateServicesContainer(List<Service> services) {
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
