package org.fi.uba.ar.ai.locations.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {

  Optional<Location> findOne(final long id);

  List<Location> findByNameIgnoreCaseContaining(final String name);

  Location findByNameIgnoreCase(final String name);

  List<Location> findByCoordinateLatitudeBetweenAndCoordinateLongitudeBetween(final double latDown,
      final double latUp,
      final double lngDown, final double lngUp);
}
