package org.fi.uba.ar.ai.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.ACCOUNT, caption = "Profile")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class ProfileView extends CustomComponent implements View {

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  public ProfileView(final UserRepository userRepository) {
    this.userRepository = userRepository;
    VerticalLayout root = new VerticalLayout();
    setCompositionRoot(root);
    root.addComponent(new Label("Hi " + SpringContextUserHolder.getUser().getFirstName() + "!"));
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

  }
}
