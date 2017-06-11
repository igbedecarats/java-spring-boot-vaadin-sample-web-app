package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;

public class ServiceComponent extends VerticalLayout {

  private Label name = new Label();
  private Label description = new Label();
  private Label category = new Label();
  private Label subCategory = new Label();
  private Label locationName = new Label();
  private Label locationArea = new Label();
  private Label times = new Label();
  private Label days = new Label();
  private Button edit = new Button();
  private Button delete = new Button();

  private Service service;
  private ServiceInteractor serviceInteractor;
  private ServiceForm form;
  private MyServicesView componentContainer;

  public ServiceComponent(final Service service, final ServiceInteractor serviceInteractor,
      final ServiceForm form, final MyServicesView componentContainer) {
    Validate.notNull(service, "The Service cannot be null.");
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null.");
    Validate.notNull(form, "The form cannot be null.");
    Validate.notNull(componentContainer, "The Component Container cannot be null.");

    this.form = form;
    this.service = service;
    this.serviceInteractor = serviceInteractor;
    this.componentContainer = componentContainer;

    name.setCaption("<h2> Servicio </h2> ");
    name.setCaptionAsHtml(true);
    name.setValue(service.getName());
    name.setContentMode(ContentMode.TEXT);
    name.setStyleName(ValoTheme.LABEL_H3);

    description.setCaption("<h2> Descripción </h2>");
    description.setCaptionAsHtml(true);
    description.setValue(service.getDescription());

    HorizontalLayout categoriesAndLocationContainer = new HorizontalLayout();
    HorizontalLayout categoriesContainer = new HorizontalLayout();
    categoriesContainer.setCaption("<h2> Categoría </h2>");
    categoriesContainer.setCaptionAsHtml(true);
    category.setValue(service.getCategory().getName());
    String subCategoryName = service.getSubCategory().getName();
    subCategory
        .setValue(StringUtils.isNotBlank(subCategoryName) ? subCategoryName : StringUtils.EMPTY);
    categoriesContainer.addComponents(category, subCategory);
    HorizontalLayout locationsContainer = new HorizontalLayout();
    locationsContainer.setCaption("<h2> Ubicación </h2>");
    locationsContainer.setCaptionAsHtml(true);
    locationArea.setValue(service.getLocation().getArea().name());
    locationName.setValue(service.getLocation().getName());
    locationsContainer.addComponents(locationArea, locationName);
    categoriesAndLocationContainer.addComponents(categoriesContainer, locationsContainer);

    HorizontalLayout dayTimeContainer = new HorizontalLayout();
    dayTimeContainer.setCaption("<h2> Días y Horarios de Atención </h2>");
    dayTimeContainer.setCaptionAsHtml(true);
    days.setValue(service.getLocalizedStartDay() + " a " + service.getLocalizedEndDay());
    times.setValue(service.getStartTime() + " a " + service.getEndTime());
    dayTimeContainer.addComponents(days, times);

    HorizontalLayout buttonsContainer = new HorizontalLayout();
    edit.setIcon(VaadinIcons.EDIT);
    edit.addClickListener(e -> this.edit());
    delete.setIcon(VaadinIcons.CLOSE);
    delete.addClickListener(e -> this.delete());
    buttonsContainer.addComponents(edit, delete);

    addComponents(name, description, categoriesAndLocationContainer, dayTimeContainer,
        buttonsContainer);

    setSizeUndefined();
  }

  private void delete() {
    serviceInteractor.delete(service);
    componentContainer.updateList();
    Notification
        .show("Success!", Type.HUMANIZED_MESSAGE);
  }

  private void edit() {
    form.setService(service);
    form.setVisible(true);
  }

}
