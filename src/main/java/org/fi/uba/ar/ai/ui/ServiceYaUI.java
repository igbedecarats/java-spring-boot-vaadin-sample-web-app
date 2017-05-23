package org.fi.uba.ar.ai.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
@Theme("valo")
public class ServiceYaUI extends UI {

  @Override
  protected void init(VaadinRequest vaadinRequest) {

    final VerticalLayout layout = new VerticalLayout();

    final TextField textField = new TextField();
    textField.setCaption("Enter your name here");

    Button button = new Button("Click Me");
    button.addClickListener(clickEvent -> {
      layout.addComponent(new Label("Hello " + textField.getValue()));
    });

    layout.addComponents(textField, button);

    setContent(layout);

  }
}
