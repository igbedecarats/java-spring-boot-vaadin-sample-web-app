package org.fi.uba.ar.ai.ui.screens;

import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.fi.uba.ar.ai.ui.events.GoToSignUp;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.vaadin.spring.annotation.PrototypeScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SuccessfulLoginEvent;

@PrototypeScope
@SpringComponent
public class LoginScreen extends CustomComponent {

  private final VaadinSecurity vaadinSecurity;
  private final EventBus.SessionEventBus eventBus;

  private TextField userName;

  private PasswordField passwordField;

  private Button login;

  private Button signUp;

  private Label loginFailedLabel;

  private Label loggedOutLabel;

  @Autowired
  public LoginScreen(VaadinSecurity vaadinSecurity, EventBus.SessionEventBus eventBus) {
    this.vaadinSecurity = vaadinSecurity;
    this.eventBus = eventBus;
    initLayout();
  }

  public void setLoggedOut(boolean loggedOut) {
    //loggedOutLabel.setVisible(loggedOut);
    if (loggedOut) {
      Notification.show("Hasta Pronto!", Type.HUMANIZED_MESSAGE);
    }
  }

  private void initLayout() {
    FormLayout loginForm = new FormLayout();
    loginForm.setSizeUndefined();
    userName = new TextField("Usuario");
    passwordField = new PasswordField("Password");
    login = new Button("Entrar");
    signUp = new Button("Registrarse");
    loginForm.addComponent(userName);
    loginForm.addComponent(passwordField);
    loginForm.addComponent(login);
    loginForm.addComponent(signUp);
    login.addStyleName(ValoTheme.BUTTON_PRIMARY);
    login.setDisableOnClick(true);
    login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    login.addClickListener((ClickListener) event -> login());
    signUp.addStyleName(ValoTheme.BUTTON_LINK);
    signUp.setDisableOnClick(true);
    signUp.addClickListener((ClickListener) event -> signUp());

    VerticalLayout loginLayout = new VerticalLayout();
    loginLayout.setSizeUndefined();

    loginFailedLabel = new Label();
    loginLayout.addComponent(loginFailedLabel);
    loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
    loginFailedLabel.setSizeUndefined();
    loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
    loginFailedLabel.setVisible(false);

    loggedOutLabel = new Label("Good bye!");
    loginLayout.addComponent(loggedOutLabel);
    loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
    loggedOutLabel.setSizeUndefined();
    loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
    loggedOutLabel.setVisible(false);

    loginLayout.addComponent(loginForm);
    loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

    VerticalLayout rootLayout = new VerticalLayout(loginLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
    setCompositionRoot(rootLayout);
    setSizeFull();
  }

  private void signUp() {
    eventBus.publish(this, new GoToSignUp(getUI()));
  }

  private void login() {
    try {
      loggedOutLabel.setVisible(false);

      String password = passwordField.getValue();
      passwordField.setValue("");

      final Authentication authentication = vaadinSecurity.login(userName.getValue(), password);
      eventBus.publish(this, new SuccessfulLoginEvent(getUI(), authentication));
    } catch (AuthenticationException ex) {
      userName.focus();
      userName.selectAll();
      loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
      loginFailedLabel.setVisible(true);
    } catch (Exception ex) {
      Notification
          .show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
      LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
    } finally {
      login.setEnabled(true);
    }
  }
}
