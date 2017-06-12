package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Search", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
public class SearchServicesView extends CustomComponent implements View {

  private QuotationForm quotationForm;

  private User loggedUser;

  private QuotationInteractor quotationInteractor;

  private ServiceInteractor serviceInteractor;

  private VerticalLayout servicesContainer = new VerticalLayout();

  @Autowired
  public SearchServicesView(QuotationInteractor quotationInteractor,
      ServiceInteractor serviceInteractor) {
    Validate.notNull(quotationInteractor, "The Quotation Interactor cannot be null");
    Validate.notNull(serviceInteractor, "The Service Interactor cannot be null");
    this.quotationInteractor = quotationInteractor;
    this.serviceInteractor = serviceInteractor;
    loggedUser = SpringContextUserHolder.getUser();
    VerticalLayout searchLayout = new VerticalLayout();
    TextField searchTextField = new TextField();
    searchTextField.addValueChangeListener(e -> simpleSearch(searchTextField.getValue()));
    searchTextField.setWidth("600px");
    CheckBox advanceSearch = new CheckBox("Búsqueda avanzada");
    searchLayout.addComponents(searchTextField, advanceSearch);
    searchLayout.setSizeUndefined();
    searchLayout.setComponentAlignment(searchTextField, Alignment.TOP_CENTER);
    searchLayout.setComponentAlignment(advanceSearch, Alignment.TOP_CENTER);
    VerticalLayout rootLayout = new VerticalLayout();
    rootLayout.addComponent(searchLayout);
    rootLayout.setSizeFull();
    rootLayout.setComponentAlignment(searchLayout, Alignment.TOP_CENTER);

    Panel panel = new Panel("Services");
    panel.setWidth("500px");
    panel.setHeight("500px");
    servicesContainer.setSizeUndefined();
    panel.setContent(servicesContainer);
    quotationForm = new QuotationForm(quotationInteractor);
    quotationForm.setVisible(false);
    HorizontalLayout servicesLayout = new HorizontalLayout(panel, quotationForm);
    servicesLayout.setSizeFull();
    rootLayout.addComponent(servicesLayout);
    setCompositionRoot(rootLayout);
    simpleSearch("");
  }

  private void simpleSearch(String value) {
    List<Service> services = serviceInteractor.findAll(value);
    servicesContainer.removeAllComponents();
    services.stream()
        .forEach(service -> servicesContainer
            .addComponent(new SearchServiceComponent(service, loggedUser, quotationForm)));
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent event) {
  }
}
