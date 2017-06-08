package org.fi.uba.ar.ai.ui.views.admin;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.stream.Collectors;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRole;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;

public class UserAdminForm extends FormLayout {

  private UserInteractor userInteractor;
  private LocationInteractor locationInteractor;
  private UserAdminView formContainer;
  private Binder<User> binder = new Binder<>(User.class);
  private User user;
  private User loggedUser;

  private TextField username = new TextField("Username");
  private TextField firstName = new TextField("First name");
  private TextField lastName = new TextField("Last name");
  private TextField email = new TextField("Email");
  private TextField password = new TextField("Password");
  private NativeSelect<UserRole> role = new NativeSelect<>("Role");
  private final List<String> existingLocations;
  private NativeSelect<String> locations = new NativeSelect<>("Location");
  private Button save = new Button("Save");
  private Button delete = new Button("Delete");

  public UserAdminForm(User loggedUser, UserInteractor userInteractor,
      LocationInteractor locationInteractor, UserAdminView formContainer) {
    this.loggedUser = loggedUser;
    this.userInteractor = userInteractor;
    this.locationInteractor = locationInteractor;
    this.formContainer = formContainer;
    setSizeUndefined();
    HorizontalLayout buttons = new HorizontalLayout(save, delete);
    addComponents(username, firstName, lastName, email, password, role, locations, buttons);
    role.setItems(UserRole.values());
    role.setSelectedItem(UserRole.CLIENT);
    role.setEmptySelectionAllowed(false);
    existingLocations = this.locationInteractor.findAll().stream()
        .map(location -> location.getName()).collect(
            Collectors.toList());
    locations.setItems(existingLocations);
    locations.setSelectedItem(existingLocations.stream().findFirst().get());
    locations.setEmptySelectionAllowed(false);
    save.setStyleName(ValoTheme.BUTTON_PRIMARY);
    save.setClickShortcut(KeyCode.ENTER);
    binder.bindInstanceFields(this);
    save.addClickListener(e -> this.save());
    delete.addClickListener(e -> this.delete());
  }

  private void delete() {
    userInteractor.delete(user);
    formContainer.updateList();
    setVisible(false);
  }

  public void setUser(final User user) {
    this.user = user;
    binder.setBean(user);
    Location location = user.getLocation();
    if (location != null) {
      locations.setSelectedItem(location.getName());
    } else {
      locations.setSelectedItem(existingLocations.stream().findFirst().get());
    }
    if (user.getRole() == null) {
      role.setSelectedItem(UserRole.CLIENT);
    }
    // Show delete button for only users already in the database
    delete.setVisible(user.getId() != 0 && isLoggedUserSelectedUser(user));
    setVisible(true);
    username.selectAll();
    username.setEnabled(isLoggedUserSelectedUser(user));
    password.setEnabled(isLoggedUserSelectedUser(user));
  }

  private boolean isLoggedUserSelectedUser(User user) {
    return loggedUser.getId() != user.getId();
  }

  private void save() {
    Location location = locationInteractor.findByName(locations.getSelectedItem().get());
    user.setLocation(location);
    userInteractor.save(user);
    formContainer.updateList();
    setVisible(false);
  }
}
