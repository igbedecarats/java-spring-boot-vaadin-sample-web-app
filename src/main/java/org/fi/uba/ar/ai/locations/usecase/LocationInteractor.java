package org.fi.uba.ar.ai.locations.usecase;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.fi.uba.ar.ai.locations.domain.Coordinate;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationRepository;

@AllArgsConstructor
public class LocationInteractor {

  private LocationRepository repository;

  private GeoApiContext geoApiContext;

  public List<Location> findAll() {
    return repository.findAll();
  }

  public Location findByName(final String locationName) {
    return repository.findByName(locationName);
  }

  public List<Location> findWithNameLike(final String locationName) {
    return repository.findByNameIgnoreCaseContaining(locationName);
  }

  public Location create(final CreateLocationRequest request) {
    Coordinate coordinateForLocation;
    try {
      coordinateForLocation = findCoordinateForLocation(request.getName());

    } catch (Exception e) {
      coordinateForLocation = new Coordinate();
    }
    return repository.save(new Location(request.getName(), request.getArea(),
        coordinateForLocation));
  }

  private Coordinate findCoordinateForLocation(final String name) {
    List<GeocodingResult> googleLocations = this.findGoogleLocations(name);
    GeocodingResult geocodingResult = googleLocations.stream().filter(this::isValidGeoCodingResult)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Location couldn't be found."));
    return new Coordinate(geocodingResult.geometry.location.lat,
        geocodingResult.geometry.location.lng);
  }

  private boolean isValidGeoCodingResult(final GeocodingResult geocodingResult) {
    return Arrays.asList(geocodingResult.types).stream()
        .anyMatch(addressType -> addressType.equals(AddressType.LOCALITY) || addressType
            .equals(AddressType.SUBLOCALITY)) &&
        Arrays.asList(geocodingResult.addressComponents).stream()
            .anyMatch(addressComponent -> addressComponent.longName.equalsIgnoreCase("Argentina"));
  }

  public List<GeocodingResult> findGoogleLocations(final String location) {
    try {
      return Arrays.asList(GeocodingApi.geocode(geoApiContext, location + " argentina")
          .resultType().await());

    } catch (ApiException | InterruptedException | IOException e) {
      throw new IllegalArgumentException("Unable to process Locations", e);
    }
  }

  public List<Location> findNearBy(String locationName) {
    Location location = repository.findByNameIgnoreCase(locationName);
    double latDown = location.getCoordinate().getLatitude() - 0.08;
    double latTop = location.getCoordinate().getLatitude() + 0.08;
    double lngDown = location.getCoordinate().getLongitude() - 0.08;
    double lngTop = location.getCoordinate().getLongitude() + 0.08;
    return repository
        .findByCoordinateLatitudeBetweenAndCoordinateLongitudeBetween(latDown, latTop, lngDown,
            lngTop);
  }

  public void delete(Location location) {
    repository.delete(location);
  }

  public Location save(Location location) {
    Coordinate coordinateForLocation;
    try {
      coordinateForLocation = findCoordinateForLocation(location.getName());

    } catch (Exception e) {
      coordinateForLocation = new Coordinate();
    }
    if (coordinateForLocation.getLatitude() != 0 & coordinateForLocation.getLongitude() != 0) {
      location.setCoordinate(coordinateForLocation);
    }
    return repository.save(location);
  }

  public Optional<Location> find(long locationId) {
    return repository.findOne(locationId);
  }
}
