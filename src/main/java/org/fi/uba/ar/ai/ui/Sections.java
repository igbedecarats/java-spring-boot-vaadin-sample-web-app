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
