package org.fi.uba.ar.ai.services.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ServiceSubCategoryRepository extends CrudRepository<ServiceSubCategory, Long> {

  List<ServiceSubCategory> findByIdIn(final List<Long> ids);

  Optional<ServiceSubCategory> findOne(final long id);

}
