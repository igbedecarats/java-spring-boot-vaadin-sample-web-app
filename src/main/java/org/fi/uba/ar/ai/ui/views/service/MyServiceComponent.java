package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.views.global.AbstractServiceComponent;
import org.fi.uba.ar.ai.users.domain.User;
import org.vaadin.viritin.button.ConfirmButton;

public class MyServiceComponent extends AbstractServiceComponent {

  private Button edit = new Button();
  private Button delete = new ConfirmButton(VaadinIcons.TRASH,
      "¿Estas seguro que queres borrar este servicio?", this::delete);

  private Service service;
  private ServiceInteractor serviceInteractor;
  private ServiceForm form;
  private MyServicesView componentContainer;

  public MyServiceComponent(final Service service, User loggedUser,
      final ServiceInteractor serviceInteractor,
      final ServiceForm form, final MyServicesView componentContainer) {
    super(service, loggedUser, serviceInteractor);
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
    buttonsContainer.addComponents(edit, delete);
    addButtonsToProviderContainer(buttonsContainer);

    setSizeUndefined();
  }

  private void delete() {
    try {
      serviceInteractor.delete(service);
      componentContainer.updateList();
      Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  private void edit() {
    form.setService(service);
    form.openInModalPopup();
  }
}
