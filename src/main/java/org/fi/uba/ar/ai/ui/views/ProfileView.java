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
