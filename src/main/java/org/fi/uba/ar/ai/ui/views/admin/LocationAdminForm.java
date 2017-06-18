package org.fi.uba.ar.ai.ui.views.admin;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.vaadin.viritin.button.ConfirmButton;

public class LocationAdminForm extends FormLayout {

  private LocationInteractor locationInteractor;
  private LocationAdminView formContainer;
  private Location location;
  private Binder<Location> binder = new Binder<>(Location.class);

  private TextField name = new TextField("Nombre");
  private NativeSelect<LocationArea> area = new NativeSelect<>("Area");
  private Label latitude = new Label();
  private Label longitude = new Label();
  private Button save = new Button("Guardar");
  private Button delete = new ConfirmButton(VaadinIcons.TRASH,
      "Estas seguro que queres borrarlo?", this::delete);

  public LocationAdminForm(LocationInteractor locationInteractor,
      LocationAdminView formContainer) {
    this.locationInteractor = locationInteractor;
    this.formContainer = formContainer;
    setSizeUndefined();
    HorizontalLayout buttons = new HorizontalLayout(save, delete);
    addComponents(name, area, latitude, longitude, buttons);
    area.setItems(LocationArea.values());
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER);
    binder.bindInstanceFields(this);
    save.addClickListener(e -> this.save());
  }

  private void delete() {
    try {
      locationInteractor.delete(location);
      formContainer.updateList();
      setVisible(false);
      Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  private void save() {
    try {
      locationInteractor.save(location);
      formContainer.updateList();
      setVisible(false);
      Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  public void setLocation(Location location) {
    this.location = location;
    binder.setBean(location);
    setVisible(true);
    latitude.setValue(Double.toString(location.getLatitude()));
    longitude.setValue(Double.toString(location.getLongitude()));
    if (location.getId() == 0) {
      name.selectAll();
      name.setEnabled(true);
    } else {
      name.setEnabled(false);
    }
  }
}
