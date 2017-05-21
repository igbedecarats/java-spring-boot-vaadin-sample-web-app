package org.fi.uba.ar.ai.locations;

import com.google.maps.GeoApiContext;
import org.fi.uba.ar.ai.locations.domain.LocationRepository;
import org.fi.uba.ar.ai.locations.usecase.LocationInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LocationConfig {

  @Value("${google.maps.key}")
  private String apiKey;

  @Autowired
  private LocationRepository locationRepository;

  @Bean
  public GeoApiContext geoApiContext() {
    return new GeoApiContext().setApiKey(apiKey);
  }

  @Bean
  public LocationInteractor locationInteractor() {
    return new LocationInteractor(locationRepository, geoApiContext());
  }

}
