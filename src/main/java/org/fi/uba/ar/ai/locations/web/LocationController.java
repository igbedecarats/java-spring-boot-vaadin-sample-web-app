package org.fi.uba.ar.ai.locations.web;

import com.google.maps.model.GeocodingResult;
import java.util.List;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.usecase.CreateLocationRequest;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/api/locations")
public class LocationController {

  @Autowired
  private LocationInteractor locationInteractor;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<List<Location>> getLocations(@RequestParam final String location) {
    return ResponseEntity.ok(locationInteractor.findWithNameLike(location));
  }

  @RequestMapping(value = "/nearby", method = RequestMethod.GET)
  public ResponseEntity<List<Location>> getLocationsNearBy(@RequestParam final String location) {
    return ResponseEntity.ok(locationInteractor.findNearBy(location));
  }

  @RequestMapping(value = "/google", method = RequestMethod.GET)
  public ResponseEntity<List<GeocodingResult>> getGoogleLocations(
      @RequestParam final String location) {
    return ResponseEntity.ok(locationInteractor.findGoogleLocations(location));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<Location> createLocations(
      @RequestBody final CreateLocationHttpRequest request) {
    return ResponseEntity.ok(locationInteractor
        .create(new CreateLocationRequest(request.getName(), request.getArea())));
  }

}
