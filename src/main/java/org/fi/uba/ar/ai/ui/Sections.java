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
package org.fi.uba.ar.ai.ui;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@Component
@SideBarSections({
    @SideBarSection(id = Sections.SERVICES, caption = "Services"),
    @SideBarSection(id = Sections.ACCOUNT, caption = "Account"),
    @SideBarSection(id = Sections.ADMIN, caption = "Admin"),
    @SideBarSection(id = Sections.OPERATIONS)
})
public class Sections {

  public static final String SERVICES = "services";
  public static final String ACCOUNT = "account";
  public static final String ADMIN = "admin";
  public static final String OPERATIONS = "operations";
}
