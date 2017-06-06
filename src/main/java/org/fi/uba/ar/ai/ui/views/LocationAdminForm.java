package org.fi.uba.ar.ai.ui.views;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;

public class LocationAdminForm extends FormLayout {

  private LocationInteractor locationInteractor;
  private LocationAdminView formContainer;
  private Location location;
  private Binder<Location> binder = new Binder<>(Location.class);

  private TextField name = new TextField("Name");
  private NativeSelect<LocationArea> area = new NativeSelect<>("Area");
  private Label latitude = new Label();
  private Label longitude = new Label();
  private Button save = new Button("Save");
  private Button delete = new Button("Delete");

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
    delete.addClickListener(e -> this.delete());
  }

  private void delete() {
    locationInteractor.delete(location);
    formContainer.updateList();
    setVisible(false);
  }

  private void save() {
    locationInteractor.save(location);
    formContainer.updateList();
    setVisible(false);
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
