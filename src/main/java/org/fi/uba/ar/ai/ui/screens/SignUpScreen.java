package org.fi.uba.ar.ai.ui.screens;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;
import java.util.stream.Collectors;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRole;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

@PrototypeScope
@SpringComponent
public class SignUpScreen extends CustomComponent {

  private final VaadinSecurity vaadinSecurity;
  private final EventBus.SessionEventBus eventBus;
  private LocationInteractor locationInteractor;
  private UserInteractor userInteractor;

  private TextField username;
  private TextField firstName;
  private TextField lastName;
  private TextField email;
  private PasswordField password;
  private CheckBox isProvider;
  private List<String> existingLocations;
  private NativeSelect<String> locations;
  private Button signUpButton;

  @Autowired
  public SignUpScreen(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus,
      LocationInteractor locationInteractor, UserInteractor userInteractor) {
    this.vaadinSecurity = vaadinSecurity;
    this.eventBus = eventBus;
    this.locationInteractor = locationInteractor;
    this.userInteractor = userInteractor;
    initLayout();
  }

  private void initLayout() {
    username = new TextField("Username");
    firstName = new TextField("First name");
    lastName = new TextField("Last name");
    email = new TextField("Email");
    password = new PasswordField("Password");
    isProvider = new CheckBox("Create a Service Provider account?");
    locations = new NativeSelect<>("Location");
    signUpButton = new Button("Sign Up");

    existingLocations = this.locationInteractor.findAll().stream()
        .map(location -> location.getName()).collect(
            Collectors.toList());
    locations.setItems(existingLocations);
    locations.setSelectedItem(existingLocations.stream().findFirst().get());
    locations.setEmptySelectionAllowed(false);

    FormLayout loginForm = new FormLayout();
    loginForm.setSizeUndefined();

    loginForm.addComponents(username, firstName, lastName, email, password, isProvider, locations,
        signUpButton);
    signUpButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
    signUpButton.setDisableOnClick(true);
    signUpButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    signUpButton.addClickListener((ClickListener) event -> signUp());

    VerticalLayout loginLayout = new VerticalLayout();
    loginLayout.setSizeUndefined();

    loginLayout.addComponent(loginForm);
    loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);
    VerticalLayout rootLayout = new VerticalLayout(loginLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
    setCompositionRoot(rootLayout);
    setSizeFull();
  }

  private void signUp() {
    try {
      String passwordValue = password.getValue();
      password.setValue("");

      String locationName = locations.getSelectedItem()
          .orElseThrow(() -> new IllegalArgumentException("Please choose a location"));
      Location location = locationInteractor.findByName(locationName);

      UserRole role;
      if (isProvider.getValue()) {
        role = UserRole.PROVIDER;
      } else {
        role = UserRole.CLIENT;
      }

      User user = new User(username.getValue(), passwordValue, email.getValue(),
          firstName.getValue(), lastName.getValue(), role);
      user.setLocation(location);
      user = userInteractor.save(user);

      final Authentication authentication = vaadinSecurity.login(user.getUsername(), passwordValue);
      eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
    } catch (Exception ex) {
      username.focus();
      username.selectAll();
      Notification
          .show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
      LoggerFactory.getLogger(getClass()).error(ex.getMessage(), ex);
    }
  }

}
