package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.views.client.ServiceComponent;

public class MyServiceComponent extends ServiceComponent {

  private Button edit = new Button();
  private Button delete = new Button();

  private Service service;
  private ServiceInteractor serviceInteractor;
  private ServiceForm form;
  private MyServicesView componentContainer;

  public MyServiceComponent(final Service service, final ServiceInteractor serviceInteractor,
      final ServiceForm form, final MyServicesView componentContainer) {
    super(service);
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null.");
    Validate.notNull(form, "The form cannot be null.");
    Validate.notNull(componentContainer, "The Component Container cannot be null.");

    this.form = form;
    this.service = service;
    this.serviceInteractor = serviceInteractor;
    this.componentContainer = componentContainer;

    HorizontalLayout buttonsContainer = new HorizontalLayout();
    edit.setIcon(VaadinIcons.EDIT);
    edit.addClickListener(e -> this.edit());
    delete.setIcon(VaadinIcons.CLOSE);
    delete.addClickListener(e -> this.delete());
    buttonsContainer.addComponents(edit, delete);
    addComponent(buttonsContainer);

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
