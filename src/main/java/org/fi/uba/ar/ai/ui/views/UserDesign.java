package org.fi.uba.ar.ai.ui.views;

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

@DesignRoot
public class UserDesign extends HorizontalLayout {

  protected TextField username;
  protected TextField firstName;
  protected TextField lastName;
  protected TextField email;
  protected Button save;
  protected Button cancel;
  protected Button delete;

}
