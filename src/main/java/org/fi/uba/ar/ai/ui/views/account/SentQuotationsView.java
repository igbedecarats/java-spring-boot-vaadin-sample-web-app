package org.fi.uba.ar.ai.ui.views.account;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "quotations")
@SideBarItem(sectionId = Sections.ACCOUNT, caption = "Sent Quotations", order = 1)
@FontAwesomeIcon(FontAwesome.HOME)
public class SentQuotationsView extends CustomComponent implements View {

  private Grid<Quotation> grid = new Grid<>();

  private QuotationInteractor quotationInteractor;

  private User loggedUser;

  @Autowired
  public SentQuotationsView(QuotationInteractor quotationInteractor) {
    this.quotationInteractor = quotationInteractor;
  }

  @Override
  public void enter(ViewChangeEvent event) {
    loggedUser = SpringContextUserHolder.getUser();

    final VerticalLayout layout = new VerticalLayout();

    grid.addColumn(quotation -> quotation.getService().getName()).setCaption("Service");
    grid.addColumn(
        quotation -> quotation.getService().getProvider().getFirstName() + " " + quotation
            .getService().getProvider().getLastName()).setCaption("Provider");
    grid.addColumn(quotation -> quotation.getDescription()).setCaption("Considerations");
    grid.addColumn(quotation -> quotation.getScheduledTime().toString())
        .setCaption("Scheduled Time");
    grid.addColumn(quotation -> quotation.getStatus().getValue()).setCaption("Status");

    HorizontalLayout main = new HorizontalLayout(grid);
    main.setSizeFull();
    grid.setSizeFull();
    main.setExpandRatio(grid, 1);

    layout.addComponent(main);
    setCompositionRoot(layout);

    grid.setItems(quotationInteractor.findByClient(loggedUser));
  }
}
