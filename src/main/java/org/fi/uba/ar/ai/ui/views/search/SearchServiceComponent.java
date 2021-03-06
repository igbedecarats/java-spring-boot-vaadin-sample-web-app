package org.fi.uba.ar.ai.ui.views.search;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.fi.uba.ar.ai.ui.views.account.QuotationForm;
import org.fi.uba.ar.ai.ui.views.global.AbstractServiceComponent;
import org.fi.uba.ar.ai.users.domain.User;

public class SearchServiceComponent extends AbstractServiceComponent {

  private Button send = new Button();

  private QuotationForm form;

  private User loggedUser;

  public SearchServiceComponent(final Service service, User loggedUser,
      ServiceInteractor serviceInteractor, QuotationForm quotationForm) {
    super(service, loggedUser, serviceInteractor);
    this.loggedUser = loggedUser;

    this.form = quotationForm;
    HorizontalLayout buttonsContainer = new HorizontalLayout();
    send.setIcon(VaadinIcons.PAPERPLANE_O);
    send.addClickListener(e -> this.send());
    buttonsContainer.addComponents(send);
    buttonsContainer.setComponentAlignment(send, Alignment.BOTTOM_LEFT);
    addButtonsToProviderContainer(buttonsContainer);

    setSizeUndefined();
  }

  private void send() {
    form.show(service, loggedUser);
    form.openInModalPopup();
  }
}
