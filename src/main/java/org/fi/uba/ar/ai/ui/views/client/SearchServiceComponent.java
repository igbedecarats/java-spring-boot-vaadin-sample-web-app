package org.fi.uba.ar.ai.ui.views.client;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.ui.views.global.AbstractServiceComponent;
import org.fi.uba.ar.ai.users.domain.User;

public class SearchServiceComponent extends AbstractServiceComponent {

  private Button send = new Button();

  private QuotationForm form;


  private User loggedUser;

  public SearchServiceComponent(final Service service,
      User loggedUser,
      QuotationForm quotationForm) {
    super(service);
    this.loggedUser = loggedUser;

    this.form = quotationForm;
    HorizontalLayout buttonsContainer = new HorizontalLayout();
    send.setIcon(VaadinIcons.PAPERPLANE_O);
    send.addClickListener(e -> this.send());
    buttonsContainer.addComponents(send);
    addComponent(buttonsContainer);

    setSizeUndefined();
  }

  private void send() {
    form.show(service, loggedUser);
    form.setVisible(true);
  }
}
