package org.fi.uba.ar.ai.ui.views.service;

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
import org.fi.uba.ar.ai.global.security.SpringContextUserHolder;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.usecase.UserInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_PROVIDER")
@SpringView(name = "myquotations")
@SideBarItem(sectionId = Sections.SERVICES, caption = "Pedidos Recibidos", order = 1)
@FontAwesomeIcon(FontAwesome.COMMENTS_O)
public class ReceivedQuotationsView extends CustomComponent implements View {

  private Grid<Quotation> grid = new Grid<>();

  private QuotationInteractor quotationInteractor;

  private UserInteractor userInteractor;

  private User loggedUser;

  @Autowired
  public ReceivedQuotationsView(QuotationInteractor quotationInteractor,
      UserInteractor userInteractor) {
    this.quotationInteractor = quotationInteractor;
    this.userInteractor = userInteractor;
  }

  @Override
  public void enter(ViewChangeEvent event) {
    loggedUser = SpringContextUserHolder.getUser();

    final VerticalLayout layout = new VerticalLayout();

    grid.addColumn(quotation -> quotation.getService().getName()).setCaption("Servicio");
    grid.addColumn(
        quotation -> quotation.getClient().getFirstName() + " " + quotation.getClient()
            .getLastName()).setCaption("Cliente");
    grid.addColumn(
        quotation -> userInteractor.calculateUserRating(quotation.getClient()).getRating())
        .setCaption("Calificacion");
    grid.addColumn(quotation -> quotation.getDescription()).setCaption("Consideraciones");
    grid.addColumn(quotation -> quotation.getScheduledTime().toString())
        .setCaption("Tiempo Agendado");
    grid.addColumn(quotation -> quotation.getStatus().getValue()).setCaption("Estado");
    grid.addColumn(person -> "Aprobar",
        new ButtonRenderer(clickEvent -> {
          approve(clickEvent.getItem());
          updateGrid();
        }));
    grid.addColumn(person -> "Rechazar",
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
    try {
      Quotation quotation = (Quotation) item;
      if (quotation.isCreated()) {
        quotationInteractor.decline(quotation);
        Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
      } else {
        Notification
            .show("Quotation already marked as " + quotation.getStatus(), Type.WARNING_MESSAGE);
      }
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  private void approve(Object item) {
    try {
      Quotation quotation = (Quotation) item;
      if (quotation.isCreated()) {
        quotationInteractor.approve(quotation);
        Notification.show("Éxito!", Type.HUMANIZED_MESSAGE);
      } else {
        Notification
            .show("Quotation already marked as " + quotation.getStatus(), Type.WARNING_MESSAGE);
      }
    } catch (Exception e) {
      Notification
          .show("Unable to process request, please contact the system admin", Type.ERROR_MESSAGE);
    }
  }

  private void updateGrid() {
    grid.setItems(quotationInteractor.findByProvider(loggedUser));
  }
}
