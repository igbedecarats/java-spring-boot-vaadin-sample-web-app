package org.fi.uba.ar.ai.ui.views.profile;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.ui.views.global.UserForm;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "profile")
@SideBarItem(sectionId = Sections.PROFILE, caption = "Perfil")
@FontAwesomeIcon(FontAwesome.USER)
public class ProfileView extends CustomComponent implements View {

  private UserInteractor userInteractor;

  private LocationInteractor locationInteractor;

  @Autowired
  public ProfileView(final UserInteractor userInteractor,
      final LocationInteractor locationInteractor) {
    Validate.notNull(userInteractor, "The User Interactor cannot be null.");
    Validate.notNull(locationInteractor, "The Location Interactor cannot be null.");
    this.userInteractor = userInteractor;
    this.locationInteractor = locationInteractor;
    User loggedUser = SpringContextUserHolder.getUser();
    UserForm form = new UserForm(loggedUser, this.userInteractor, this.locationInteractor, null);
    form.setUser(loggedUser);
    VerticalLayout layout = new VerticalLayout();
    layout.setSizeUndefined();
    layout.addComponent(form);
    layout.setComponentAlignment(form, Alignment.TOP_CENTER);
    VerticalLayout rootLayout = new VerticalLayout(layout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(layout, Alignment.MIDDLE_CENTER);
    setCompositionRoot(rootLayout);
    setSizeFull();
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

  }
}
