package org.fi.uba.ar.ai.services.domain;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ServiceCategoryRepository extends CrudRepository<ServiceCategory, Long> {

  Optional<ServiceCategory> findOne(final long id);

}
