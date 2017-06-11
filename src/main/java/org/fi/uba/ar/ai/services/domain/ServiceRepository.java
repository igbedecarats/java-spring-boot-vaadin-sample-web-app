package org.fi.uba.ar.ai.services.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<Service, Long> {

  List<Service> findByProviderIdAndNameIgnoreCaseContaining(final long id, final String name);

  List<Service> findByProviderId(long id);

  List<Service> findByNameIgnoreCaseContaining(String value);
}
