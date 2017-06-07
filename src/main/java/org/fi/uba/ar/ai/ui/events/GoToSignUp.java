package org.fi.uba.ar.ai.ui.events;

import com.vaadin.ui.UI;
import org.springframework.context.ApplicationEvent;

public class GoToSignUp extends ApplicationEvent {

  /**
   * Create a new ApplicationEvent.
   *
   * @param source the object on which the event initially occurred (never {@code null})
   */
  public GoToSignUp(UI source) {
    super(source);
  }

  @Override
  public UI getSource() {
    return (UI) super.getSource();
  }
}
