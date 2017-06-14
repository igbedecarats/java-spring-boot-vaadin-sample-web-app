package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.data.Binder;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import java.util.List;
import java.util.stream.Collectors;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.domain.ServiceCategory;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class ServiceForm extends AbstractForm<Service> {

  private User loggedUser;
  private Service service;
  private ServiceInteractor serviceInteractor;
  private LocationInteractor locationInteractor;
  private final EventBus.SessionEventBus eventBus;

  private Binder<Service> binder = new Binder<>(Service.class);

  private TextField name = new TextField("Name");
  private TextField description = new TextField("Description");
  private List<String> existingCategories;
  private NativeSelect<String> categories = new NativeSelect<>("Categories");
  private List<String> existingSubCategories;
  private NativeSelect<String> subCategories = new NativeSelect<>("Sub Categories");
  private List<String> existingLocations;
  private NativeSelect<String> locations = new NativeSelect<>("Location");
  private TextField startTime = new TextField("Start Time");
  private TextField endTime = new TextField("End Time");
  private List<String> daysOfTheWeek = Service.getLocalizedDaysOfTheWeek();
  private NativeSelect<String> startDays = new NativeSelect<>("Start Day");
  private NativeSelect<String> endDays = new NativeSelect<>("End Day");

  private final HorizontalLayout categoriesContainer = new HorizontalLayout();
  private final HorizontalLayout timesContainer = new HorizontalLayout();
  private final HorizontalLayout daysContainer = new HorizontalLayout();

  @Autowired
  public ServiceForm(ServiceInteractor serviceInteractor,
      LocationInteractor locationInteractor,
      EventBus.SessionEventBus eventBus) {
    super(Service.class);
    this.loggedUser = SpringContextUserHolder.getUser();
    this.service = new Service();
    this.serviceInteractor = serviceInteractor;
    this.locationInteractor = locationInteractor;
    this.eventBus = eventBus;

    binder.bindInstanceFields(this);

    updateCategories();
    categories.setItems(existingCategories);
    categories.setEmptySelectionAllowed(false);
    categories.setSelectedItem(existingCategories.stream().findFirst().get());
    categories.addValueChangeListener(event -> {
      updateSubCategories();
      subCategories.setItems(existingSubCategories);
      subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get());
    });

    updateSubCategories();
    subCategories.setItems(existingSubCategories);
    subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get());

    categoriesContainer.addComponents(categories, subCategories);

    updateLocations();
    locations.setItems(existingLocations);
    locations.setEmptySelectionAllowed(false);

    timesContainer.addComponents(startTime, endTime);

    startDays.setItems(daysOfTheWeek);
    endDays.setItems(daysOfTheWeek);
    startDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get());
    endDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get());
    daysContainer.addComponents(startDays, endDays);

    setSavedHandler(service -> save());
    setResetHandler(service -> eventBus.publish(this, new ServiceCreatedEvent(service)));
    setModalWindowTitle("Service");
    this.setSizeUndefined();
  }

  private void updateCategories() {
    existingCategories = this.serviceInteractor.findAllCategories().stream()
        .map(category -> category.getName()).collect(
            Collectors.toList());
  }

  private void updateSubCategories() {
    if (this.categories.getSelectedItem().isPresent()) {
      ServiceCategory category = serviceInteractor
          .findCategoryByName(this.categories.getSelectedItem().get());
      this.existingSubCategories = category.getSubCategories().stream()
          .map(serviceSubCategory -> serviceSubCategory.getName()).collect(
              Collectors.toList());
    }
  }

  private void updateLocations() {
    existingLocations = this.locationInteractor.findAll().stream()
        .map(location -> location.getName()).collect(
            Collectors.toList());
  }

  private void save() {
    try {
      service.setProvider(loggedUser);
      String categoryName = categories.getSelectedItem()
          .orElseThrow(() -> new IllegalArgumentException("The Service Category is mandatory"));
      service.setCategory(serviceInteractor.findCategoryByName(categoryName));
      if (subCategories.getSelectedItem().isPresent()) {
        service.setSubCategory(service.getCategory().getSubCategories().stream().filter(
            serviceSubCategory -> serviceSubCategory.getName()
                .equals(subCategories.getSelectedItem().get()))
            .findFirst().get());
      }
      String locationName = locations.getSelectedItem()
          .orElseThrow(() -> new IllegalArgumentException("The Location is mandatory"));
      service.setLocation(locationInteractor.findByName(locationName));
      if (startDays.getSelectedItem().isPresent()) {
        service.setLocalizedStartDay(startDays.getSelectedItem().get());
      }
      if (endDays.getSelectedItem().isPresent()) {
        service.setLocalizedEndDay(endDays.getSelectedItem().get());
      }
      serviceInteractor.save(service);
      eventBus.publish(this, new ServiceCreatedEvent(service));
      Notification
          .show("Success!", Type.HUMANIZED_MESSAGE);
    } catch (Exception ex) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  public void setService(Service service) {
    this.service = service;
    binder.setBean(service);
    updateLocations();
    locations.setItems(existingLocations);
    locations.setSelectedItem(
        loggedUser.getLocation() != null ? loggedUser.getLocation().getName()
            : existingLocations.stream().findFirst().get());
    updateCategories();
    categories.setItems(existingCategories);
    if (service.getCategory() == null) {
      categories.setSelectedItem(existingCategories.stream().findFirst().get());
      updateSubCategories();
      subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get());
    } else {
      categories.setSelectedItem(service.getCategory().getName());
      updateSubCategories();
      if (service.getSubCategory() != null) {
        subCategories.setSelectedItem(service.getSubCategory().getName());
      }
    }
    if (service.getStartTime() != null) {
      startTime.setValue(service.getStartTime());
    } else {
      startTime.setValue("00:00");
    }
    if (service.getEndTime() != null) {
      endTime.setValue(service.getEndTime());
    } else {
      endTime.setValue("00:00");
    }

    startDays.setItems(daysOfTheWeek);
    endDays.setItems(daysOfTheWeek);
    startDays.setSelectedItem(service.getStartDay() != null ? service.getLocalizedStartDay()
        : daysOfTheWeek.stream().findFirst().get());
    endDays.setSelectedItem(service.getEndDay() != null ? service.getLocalizedEndDay()
        : daysOfTheWeek.stream().findFirst().get());
  }

  @Override
  protected Component createContent() {
    return new MVerticalLayout(
        new MFormLayout(name, description, categoriesContainer, locations, timesContainer,
            daysContainer).withWidth(""),
        getToolbar()).withWidth("");
  }
}
