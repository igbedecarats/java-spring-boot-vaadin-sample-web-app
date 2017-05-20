package org.fi.uba.ar.ai.services.usecase;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.domain.ServiceCategory;
import org.fi.uba.ar.ai.services.domain.ServiceCategoryRepository;
import org.fi.uba.ar.ai.services.domain.ServiceRepository;

public class ServiceInteractor {

  private ServiceRepository serviceRepository;

  private ServiceCategoryRepository serviceCategoryRepository;

  public ServiceInteractor(final ServiceRepository serviceRepository,
      final ServiceCategoryRepository serviceCategoryRepository) {
    this.serviceRepository = serviceRepository;
    this.serviceCategoryRepository = serviceCategoryRepository;
  }

  public Service find(final long serviceId) {
    Service service = serviceRepository.findOne(serviceId);
    if (service == null) {
      throw new EntityNotFoundException("The Service with id " + serviceId + " doesn't exist.");
    }
    return service;
  }

  public Set<ServiceCategory> getServiceCategories() {
    return Sets.newHashSet(serviceCategoryRepository.findAll());
  }

  public ServiceCategory getServiceCategory(long categoryId) {
    ServiceCategory category = serviceCategoryRepository.findOne(categoryId);
    if (category == null) {
      throw new EntityNotFoundException("The Category with id " + categoryId + " doesn't exist.");
    }
    return category;
  }
}
