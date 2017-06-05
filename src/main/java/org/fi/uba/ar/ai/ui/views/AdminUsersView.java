/*
 * Copyright 2015 The original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fi.uba.ar.ai.ui.views;

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
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_ADMIN")
@SpringView(name = "users")
@SideBarItem(sectionId = Sections.ADMIN, caption = "Users")
@FontAwesomeIcon(FontAwesome.COGS)
public class AdminUsersView extends CustomComponent implements View {

  private UserInteractor userInteractor;

  private LocationInteractor locationInteractor;

  private Grid<User> grid = new Grid<>(User.class);

  private UserAdminForm form;

  @Autowired
  public AdminUsersView(final UserInteractor userInteractor,
      final LocationInteractor locationInteractor) {
    Validate.notNull(userInteractor, "The User Interactor cannot be null.");
    Validate.notNull(locationInteractor, "The Location Interactor cannot be null.");
    this.userInteractor = userInteractor;
    this.locationInteractor = locationInteractor;

    User loggedUser = SpringContextUserHolder.getUser();
    form = new UserAdminForm(loggedUser, userInteractor, locationInteractor, this);

    final VerticalLayout layout = new VerticalLayout();

    Button addUserBtn = new Button("Add new customer");
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
