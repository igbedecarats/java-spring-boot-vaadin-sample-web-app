package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;

public class ServiceComponent extends VerticalLayout {

  private TextField name = new TextField("Name");
  private Label description;
  private TextField category = new TextField("Category");
  private TextField subCategory = new TextField("Sub Category");
  private TextField locationName = new TextField("Location");
  private TextField locationArea = new TextField("Area");
  private TextField startTime = new TextField("Start Time");
  private TextField endTime = new TextField("End Time");
  private TextField startDay = new TextField("Start Day");
  private TextField endDay = new TextField("End Day");
  private Button edit = new Button();
  private Button delete = new Button();

  private Service service;
  private ServiceInteractor serviceInteractor;
  private PopupView popUp;
  private ServicePopUpContent popUpContent;
  private MyServicesView componentContainer;

  public ServiceComponent(final Service service, final ServiceInteractor serviceInteractor,
      PopupView popUp,
      ServicePopUpContent popUpContent,
      final MyServicesView componentContainer) {
    Validate.notNull(service, "The Service cannot be null.");
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null.");
    Validate.notNull(popUp, "The popUp cannot be null.");
    Validate.notNull(popUpContent, "The popUpContent cannot be null.");
    Validate.notNull(componentContainer, "The Component Container cannot be null.");

    this.popUp = popUp;
    this.popUpContent = popUpContent;
    this.service = service;
    this.serviceInteractor = serviceInteractor;
    this.componentContainer = componentContainer;

    name.setEnabled(false);
    name.setValue(service.getName());

    VerticalLayout descriptionContainer = new VerticalLayout();
    Panel descriptionPanel = new Panel("Description");
    descriptionPanel.setHeight(100.0f, Unit.PERCENTAGE);
    descriptionContainer.setWidth(500, Unit.PIXELS);
    descriptionContainer.setSpacing(false);
    description = new Label(service.getDescription(), ContentMode.HTML);
    descriptionContainer.addComponent(description);
    descriptionPanel.setContent(descriptionContainer);

    HorizontalLayout categoriesContainer = new HorizontalLayout();
    category.setEnabled(false);
    category.setValue(service.getCategory().getName());
    subCategory.setEnabled(false);
    String subCategoryName = service.getSubCategory().getName();
    subCategory
        .setValue(StringUtils.isNotBlank(subCategoryName) ? subCategoryName : StringUtils.EMPTY);
    categoriesContainer.addComponents(category, subCategory);

    HorizontalLayout locationsContainer = new HorizontalLayout();
    locationName.setEnabled(false);
    locationName.setValue(service.getLocation().getName());
    locationArea.setEnabled(false);
    locationArea.setValue(service.getLocation().getArea().name());
    locationsContainer.addComponents(locationName, locationArea);

    HorizontalLayout timesContainer = new HorizontalLayout();
    startTime.setEnabled(false);
    startTime.setValue(service.getStartTime());
    endTime.setEnabled(false);
    endTime.setValue(service.getEndTime());
    timesContainer.addComponents(startTime, endTime);

    HorizontalLayout daysContainer = new HorizontalLayout();
    startDay.setEnabled(false);
    startDay.setValue(service.getLocalizedStartDay());
    endDay.setEnabled(false);
    endDay.setValue(service.getLocalizedEndDay());
    daysContainer.addComponents(startDay, endDay);

    HorizontalLayout buttonsContainer = new HorizontalLayout();
    edit.setIcon(VaadinIcons.EDIT);
    edit.addClickListener(e -> this.edit());
    delete.setIcon(VaadinIcons.CLOSE);
    delete.addClickListener(e -> this.delete());
    buttonsContainer.addComponents(edit, delete);

    addComponents(name, description, categoriesContainer, locationsContainer, timesContainer,
        daysContainer, buttonsContainer);

    setSizeUndefined();
  }

  private void delete() {
    serviceInteractor.delete(service);
    componentContainer.updateList();
  }

  private void edit() {
    popUpContent.setService(service);
    popUp.setPopupVisible(true);
  }

}
