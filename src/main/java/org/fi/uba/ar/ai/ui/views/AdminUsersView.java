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
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalSplitPanel;
import javax.annotation.PostConstruct;
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

  private HorizontalSplitPanel splitter = new HorizontalSplitPanel();
  private Grid<User> grid = new Grid(User.class);
  UserView editor = new UserView(this::saveUser, this::editSelectedUser, this::deleteUser);

  @Autowired
  public AdminUsersView(final UserInteractor userInteractor) {
    Validate.notNull(userInteractor, "The User Interactor cannot be null.");
    this.userInteractor = userInteractor;
    splitter.setSizeFull();
    grid.setSizeFull();
    editor.setSizeFull();

    splitter.setFirstComponent(grid);
    splitter.setSecondComponent(editor);

    setCompositionRoot(splitter);
  }

  @PostConstruct
  public void init() {
    grid.asSingleSelect().addValueChangeListener(evt -> {
      editSelectedUser();
    });
    listUsers();
    grid.setColumns("firstName","lastName","email");
    selectDefault();
  }

  private void saveUser(User person) {
    User newUser = userInteractor.save(person);
    listUsers();
    grid.select(newUser);
  }

  private void deleteUser(User person) {
    userInteractor.delete(person);
    listUsers();
    selectDefault();
  }

  private void editSelectedUser() {
    User selected = grid.asSingleSelect().getValue();
    if(selected != null) {
      editor.setUser(userInteractor.find(selected.getId()));
    }else {
      selectDefault();
    }
  }

  private void selectDefault() {
    grid.select(userInteractor.findAll().stream().findFirst().get());
  }

  private void listUsers() {
    grid.setItems(userInteractor.findAll());
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
  }
}
