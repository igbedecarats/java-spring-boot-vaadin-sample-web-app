package org.fi.uba.ar.ai.ui.views.global;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ButtonRenderer;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@SpringView(name = "contracts")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Contracts", order = 2)
@FontAwesomeIcon(FontAwesome.HOME)
public class ContractsView extends CustomComponent implements View {

  private Grid<Contract> grid = new Grid<>();

  private ContractInteractor contractInteractor;

  private User loggedUser;

  @Autowired
  public ContractsView(ContractInteractor contractInteractor) {
    this.contractInteractor = contractInteractor;
  }

  @Override
  public void enter(ViewChangeEvent event) {
    loggedUser = SpringContextUserHolder.getUser();

    final VerticalLayout layout = new VerticalLayout();

    grid.addColumn(contract -> contract.getService().getName()).setCaption("Service");
    grid.addColumn(
        contract -> contract.getClient().getFirstName() + " " + contract
            .getClient().getLastName()).setCaption("Client");
    grid.addColumn(
        quotation -> quotation.getService().getProvider().getFirstName() + " " + quotation
            .getService().getProvider().getLastName()).setCaption("Provider");

    grid.addColumn(quotation -> quotation.getScheduledTime().toString())
        .setCaption("Scheduled Time");
    grid.addColumn(quotation -> quotation.getStatus().name()).setCaption("Status");

    grid.addColumn(person -> "Done",
        new ButtonRenderer(clickEvent -> {
          done(clickEvent.getItem());
          updateGrid();
        }));

    HorizontalLayout main = new HorizontalLayout(grid);
    main.setSizeFull();
    grid.setSizeFull();
    main.addComponentsAndExpand(grid);
    layout.addComponent(main);
    setCompositionRoot(layout);

    updateGrid();
  }

  private void updateGrid() {
    grid.setItems(contractInteractor.findForUser(loggedUser));
  }

  private void done(Object item) {
    try {
      Contract contract = (Contract) item;
      contractInteractor.markAsDoneByUser(contract, loggedUser);
      Notification.show("Success!", Type.HUMANIZED_MESSAGE);
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }
}
