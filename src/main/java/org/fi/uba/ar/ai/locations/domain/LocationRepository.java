package org.fi.uba.ar.ai.locations.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

  List<Location> findByNameIgnoreCaseContaining(final String name);

  Location findByNameIgnoreCase(final String name);

  List<Location> findByCoordinateLatitudeBetweenAndCoordinateLongitudeBetween(final double latDown, final double latUp,
      final double lngDown, final double lngUp);
}
