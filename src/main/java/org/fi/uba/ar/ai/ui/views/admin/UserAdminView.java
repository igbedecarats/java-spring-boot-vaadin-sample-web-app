package org.fi.uba.ar.ai.ui.views.admin;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.ui.views.global.UserForm;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_ADMIN")
@SpringView(name = "users")
@SideBarItem(sectionId = Sections.ADMIN, caption = "Usuarios", order = 0)
@FontAwesomeIcon(FontAwesome.USERS)
public class UserAdminView extends CustomComponent implements View {

  private UserInteractor userInteractor;

  private LocationInteractor locationInteractor;

  private Grid<User> grid = new Grid<>(User.class);

  private UserForm form;

  @Autowired
  public UserAdminView(final UserInteractor userInteractor,
      final LocationInteractor locationInteractor) {
    Validate.notNull(userInteractor, "The User Interactor cannot be null.");
    Validate.notNull(locationInteractor, "The Location Interactor cannot be null.");
    this.userInteractor = userInteractor;
    this.locationInteractor = locationInteractor;

    User loggedUser = SpringContextUserHolder.getUser();
    form = new UserForm(loggedUser, this.userInteractor, this.locationInteractor,
        () -> updateList());

    final VerticalLayout layout = new VerticalLayout();

    Button addUserBtn = new Button("Agregar nuevo usuario");
    addUserBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      form.setUser(new User());
    });

    HorizontalLayout toolbar = new HorizontalLayout(addUserBtn);

    grid.setColumns("id", "username", "firstName", "lastName", "email");

    HorizontalLayout main = new HorizontalLayout(grid, form);
    main.setSizeFull();
    grid.setSizeFull();
    main.setExpandRatio(grid, 1);

    layout.addComponents(toolbar, main);

    updateList();

    setCompositionRoot(layout);

    form.setVisible(false);

    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        form.setVisible(false);
      } else {
        form.setUser(event.getValue());
      }
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
  }

  public void updateList() {
    List<User> users = userInteractor.findAll();
    grid.setItems(users);
  }
}