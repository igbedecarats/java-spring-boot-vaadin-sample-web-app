package org.fi.uba.ar.ai.ui.views.global;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.users.domain.User;

public abstract class AbstractServiceComponent extends VerticalLayout {

  protected Label name = new Label();
  protected Label description = new Label();
  protected Label category = new Label();
  protected Label subCategory = new Label();
  protected Label locationName = new Label();
  protected Label locationArea = new Label();
  protected Label times = new Label();
  protected Label days = new Label();
  protected Label provider = new Label();
  protected Label rating = new Label();
  protected Service service;

  public AbstractServiceComponent(final Service service, final User loggedUser,
      final ServiceInteractor serviceInteractor) {
    Validate.notNull(service, "The Service cannot be null.");
    Validate.notNull(loggedUser, "The User cannot be null.");
    Validate.notNull(serviceInteractor, "The ServiceInteractor cannot be null.");
    this.service = service;

    name.setCaption("<h2> Servicio </h2> ");
    name.setCaptionAsHtml(true);
    name.setValue(service.getName());
    name.setContentMode(ContentMode.TEXT);
    name.setStyleName(ValoTheme.LABEL_H3);
    addComponent(name);

    description.setCaption("<h2> Descripción </h2>");
    description.setCaptionAsHtml(true);
    description.setValue(service.getDescription());
    addComponent(description);

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
    addComponent(categoriesAndLocationContainer);

    HorizontalLayout dayTimeContainer = new HorizontalLayout();
    dayTimeContainer.setCaption("<h2> Días y Horarios de Atención </h2>");
    dayTimeContainer.setCaptionAsHtml(true);
    days.setValue(service.getLocalizedStartDay() + " a " + service.getLocalizedEndDay());
    times.setValue(service.getStartTime() + " a " + service.getEndTime());
    dayTimeContainer.addComponents(days, times);
    addComponent(dayTimeContainer);

    HorizontalLayout providerContainer = new HorizontalLayout();
    provider
        .setValue(service.getProvider().getFirstName() + " " + service.getProvider().getLastName());
    provider.setVisible(!loggedUser.equals(service.getProvider()));
    provider.setStyleName(ValoTheme.LABEL_BOLD);
    rating
        .setValue(Float.toString(serviceInteractor.calculateRate(service, loggedUser).getRating()));
    providerContainer.addComponents(provider, rating);
    rating.setStyleName(ValoTheme.LABEL_BOLD);
    addComponent(providerContainer);

    setSizeUndefined();
  }

}
