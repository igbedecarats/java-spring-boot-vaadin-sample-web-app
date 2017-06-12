package org.fi.uba.ar.ai.ui.views.service;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_PROVIDER")
@SpringView(name = "myquotations")
@SideBarItem(sectionId = Sections.ADMIN, caption = "My Pending Quotations", order = 1)
@FontAwesomeIcon(FontAwesome.COGS)
public class ProviderQuotationsView extends CustomComponent implements View {

  private Grid<Quotation> grid = new Grid<>();

  private QuotationInteractor quotationInteractor;

  private User loggedUser;

  @Autowired
  public ProviderQuotationsView(QuotationInteractor quotationInteractor) {
    this.quotationInteractor = quotationInteractor;
  }

  @Override
  public void enter(ViewChangeEvent event) {
    loggedUser = SpringContextUserHolder.getUser();

    final VerticalLayout layout = new VerticalLayout();

    grid.addColumn(quotation -> quotation.getService().getName()).setCaption("Service");
    grid.addColumn(
        quotation -> quotation.getClient().getFirstName() + " " + quotation.getClient()
            .getLastName()).setCaption("Client");
    grid.addColumn(quotation -> quotation.getDescription()).setCaption("Considerations");
    grid.addColumn(quotation -> quotation.getScheduledTime().toString())
        .setCaption("Scheduled Time");
    grid.addColumn(quotation -> quotation.getStatus().name()).setCaption("Status");
    grid.addColumn(person -> "Approve",
        new ButtonRenderer(clickEvent -> {
          approve(clickEvent.getItem());
          updateGrid();
        }));
    grid.addColumn(person -> "Decline",
        new ButtonRenderer(clickEvent -> {
          decline(clickEvent.getItem());
          updateGrid();
        }));

    HorizontalLayout main = new HorizontalLayout(grid);
    main.setSizeFull();
    grid.setSizeFull();
    main.setExpandRatio(grid, 1);

    layout.addComponent(main);
    setCompositionRoot(layout);

    updateGrid();
  }

  private void decline(Object item) {
    Quotation quotation = (Quotation) item;
    quotationInteractor.decline(quotation);
  }

  private void approve(Object item) {
    Quotation quotation = (Quotation) item;


    quotationInteractor.approve(quotation);
  }

  private void updateGrid() {
    grid.setItems(quotationInteractor.findByProvider(loggedUser));
  }
}
