package org.fi.uba.ar.ai.services.domain;

import java.util.List;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Long> {

  List<Service> findByProviderIdAndNameIgnoreCaseContaining(final long id, final String name);

  List<Service> findByProviderId(long id);

  List<Service> findByNameIgnoreCaseContaining(String value);

  List<Service> findByNameIgnoreCaseContainingAndLocationAreaInAndCategoryNameInAndStartDayGreaterThanEqualAndEndDayLessThanEqual(
      String likeFilter, List<LocationArea> searchAreas, List<String> searchCategories,
      Integer searchStartDay, Integer searchEndDays);

  List<Service> findByNameIgnoreCaseContainingAndLocationIn(String likeFilter,
      List<Location> locations);
}
