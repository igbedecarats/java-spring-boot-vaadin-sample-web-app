package org.fi.uba.ar.ai.ui.views;

import com.vaadin.data.Binder;
import org.fi.uba.ar.ai.users.domain.User;

public class UserView extends UserDesign {

  public interface UserSaveListener {

    void saveUser(User user);
  }

  public interface UserCancelListener {

    void resetUser();
  }

  public interface UserDeleteListener {

    void deleteUser(User user);
  }

  Binder<User> binder = new Binder(User.class);

  public UserView(UserSaveListener saveEvt,
      UserCancelListener cancelEvt,
      UserDeleteListener delEvt) {
    binder.bindInstanceFields(this);

    save.addClickListener(evt -> saveEvt.saveUser(binder.getBean()));

    cancel.addClickListener(evt -> cancelEvt.resetUser());

    delete.addClickListener(evt -> delEvt.deleteUser(binder.getBean()));
  }

  public void setUser(User selectedRow) {
    binder.setBean(selectedRow);
  }

}