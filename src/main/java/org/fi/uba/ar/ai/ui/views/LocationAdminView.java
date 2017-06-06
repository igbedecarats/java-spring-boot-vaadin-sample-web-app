package org.fi.uba.ar.ai.ui.views;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.List;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.fi.uba.ar.ai.ui.Sections;
import org.jsoup.helper.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

@Secured("ROLE_ADMIN")
@SpringView(name = "locations")
@SideBarItem(sectionId = Sections.ADMIN, caption = "Locations")
@FontAwesomeIcon(FontAwesome.COGS)
public class LocationAdminView extends CustomComponent implements View {

  private LocationInteractor locationInteractor;

  private Grid<Location> grid = new Grid<>();

  private LocationAdminForm form;

  @Autowired
  public LocationAdminView(final LocationInteractor locationInteractor) {
    Validate.notNull(locationInteractor, "The Location Interactor cannot be null.");
    this.locationInteractor = locationInteractor;
    form = new LocationAdminForm(locationInteractor, this);

    final VerticalLayout layout = new VerticalLayout();

    Button addLocationBtn = new Button("Add new location");
    addLocationBtn.addClickListener(e -> {
      grid.asSingleSelect().clear();
      form.setLocation(new Location());
    });

    HorizontalLayout toolbar = new HorizontalLayout(addLocationBtn);

    grid.addColumn(Location::getId).setCaption("Id");
    grid.addColumn(Location::getName).setCaption("Name");
    grid.addColumn(Location::getArea).setCaption("Area");
    grid.addColumn(Location::getLatitude).setCaption("Latitude");
    grid.addColumn(Location::getLongitude).setCaption("Longitude");

    HorizontalLayout main = new HorizontalLayout(grid, form);
    main.setSizeFull();
    grid.setSizeFull();
    main.setExpandRatio(grid, 1);

    layout.addComponents(toolbar, main);

    updateList();

    setCompositionRoot(layout);

    form.setVisible(false);

    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() == null) {
        form.setVisible(false);
      } else {
        form.setLocation(event.getValue());
      }
    });
  }

  @Override
  public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
  }

  public void updateList() {
    List<Location> locations = locationInteractor.findAll();
    grid.setItems(locations);
  }
}
