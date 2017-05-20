package org.fi.uba.ar.ai.services.usecase;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.services.domain.ServiceCategory;
import org.fi.uba.ar.ai.services.domain.ServiceCategoryRepository;
import org.fi.uba.ar.ai.services.domain.ServiceRepository;
import org.fi.uba.ar.ai.services.domain.ServiceSubCategory;
import org.fi.uba.ar.ai.services.domain.ServiceSubCategoryRepository;
import org.fi.uba.ar.ai.users.domain.User;
import org.fi.uba.ar.ai.users.domain.UserRepository;

public class ServiceInteractor {

  private ServiceRepository serviceRepository;

  private ServiceCategoryRepository serviceCategoryRepository;

  private ServiceSubCategoryRepository serviceSubCategoryRepository;

  private UserRepository userRepository;

  public ServiceInteractor(ServiceRepository serviceRepository,
      ServiceCategoryRepository serviceCategoryRepository,
      ServiceSubCategoryRepository serviceSubCategoryRepository,
      UserRepository userRepository) {
    this.serviceRepository = serviceRepository;
    this.serviceCategoryRepository = serviceCategoryRepository;
    this.serviceSubCategoryRepository = serviceSubCategoryRepository;
    this.userRepository = userRepository;
  }

  public Service find(final long serviceId) {
    Service service = serviceRepository.findOne(serviceId);
    if (service == null) {
      throw new EntityNotFoundException("The Service with id " + serviceId + " doesn't exist.");
    }
    return service;
  }

  public Service create(CreateServiceRequest request) {
    Validate.notNull(request, "The CreateServiceRequest cannot be null.");
    User provider = userRepository.findOne(request.getProviderId());
    Validate
        .notNull(provider, "The provider with id " + request.getProviderId() + " doesn't exist.");
    ServiceCategory category = serviceCategoryRepository.findOne(request.getCategoryId());
    Validate
        .notNull(category, "The category with id " + request.getCategoryId() + " doesn't exist.");
    List<ServiceSubCategory> subCategories = serviceSubCategoryRepository
        .findByIdIn(request.getSubCategoryIds());
    subCategories.stream()
        .forEach(subCategory -> validateSubCategoryBelongsToCategory(subCategory, category));
    Service service = new Service(provider, request.getName(), request.getDescription(), category,
        new LinkedHashSet<>(subCategories));
    return serviceRepository.save(service);
  }

  private void validateSubCategoryBelongsToCategory(final ServiceSubCategory subCategory,
      final ServiceCategory category) {
    Validate.isTrue(category.getSubCategories().contains(subCategory),
        "The Category with id " + category.getId() + " doesn't contain the Sub Category with id "
            + subCategory.getId());
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
