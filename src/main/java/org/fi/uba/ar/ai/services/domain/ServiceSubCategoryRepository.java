package org.fi.uba.ar.ai.services.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ServiceSubCategoryRepository extends CrudRepository<ServiceSubCategory, Long> {

  List<ServiceSubCategory> findByIdIn(List<Long> ids);

}
