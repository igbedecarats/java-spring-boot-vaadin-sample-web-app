package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.stream.Collectors;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.domain.ServiceCategory;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.slf4j.LoggerFactory;

public class ServicePopUpContent implements PopupView.Content {

  private User loggedUser;
  private Service service;
  private ServiceInteractor serviceInteractor;
  private LocationInteractor locationInteractor;
  private MyServicesView popUpContainer;

  private FormLayout formLayout = new FormLayout();
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
  private Button save = new Button("Save");

  public ServicePopUpContent(User loggedUser,
      ServiceInteractor serviceInteractor,
      LocationInteractor locationInteractor,
      MyServicesView popUpContainer) {
    this.loggedUser = loggedUser;
    this.service = new Service();
    this.serviceInteractor = serviceInteractor;
    this.locationInteractor = locationInteractor;
    this.popUpContainer = popUpContainer;

    binder.bindInstanceFields(this);

    updateCategories();
    categories.setItems(existingCategories);
    categories.setSelectedItem(existingCategories.stream().findFirst().get());
    categories.addValueChangeListener(event -> {
      updateSubCategories();
      subCategories.setItems(existingSubCategories);
      subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get());
    });

    updateSubCategories();
    subCategories.setItems(existingSubCategories);
    subCategories.setSelectedItem(existingSubCategories.stream().findFirst().get());

    HorizontalLayout categoriesContainer = new HorizontalLayout();
    categoriesContainer.addComponents(categories, subCategories);

    updateLocations();
    locations.setItems(existingLocations);

    HorizontalLayout timesContainer = new HorizontalLayout();
    timesContainer.addComponents(startTime, endTime);

    HorizontalLayout daysContainer = new HorizontalLayout();
    startDays.setItems(daysOfTheWeek);
    endDays.setItems(daysOfTheWeek);
    startDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get());
    endDays.setSelectedItem(daysOfTheWeek.stream().findFirst().get());
    daysContainer.addComponents(startDays, endDays);

    HorizontalLayout buttonsContainer = new HorizontalLayout();
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER);
    save.addClickListener(e -> this.save());
    buttonsContainer.addComponents(save);

    formLayout
        .addComponents(name, description, categoriesContainer, locations, timesContainer,
            daysContainer, buttonsContainer);

    formLayout.setSizeUndefined();
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
      popUpContainer.closePopUp();
    } catch (Exception ex) {
      Notification
          .show("An unexpected error occurred: ", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
      LoggerFactory.getLogger(getClass()).error(ex.getMessage(), ex);
    }

  }

  @Override
  public String getMinimizedValueAsHTML() {
    return null;
  }

  @Override
  public Component getPopupComponent() {
    return formLayout;
  }

  public void setService(Service service) {
    this.service = service;
    binder.setBean(service);
    updateLocations();
    locations.setItems(existingLocations);
    if (service.getLocation() == null) {
      locations.setSelectedItem(
          loggedUser.getLocation() != null ? loggedUser.getLocation().getName()
              : existingLocations.stream().findFirst().get());
    } else {
      locations.setSelectedItem(service.getLocation().getName());
    }
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
}
