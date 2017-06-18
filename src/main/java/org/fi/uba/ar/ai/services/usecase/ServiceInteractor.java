package org.fi.uba.ar.ai.services.usecase;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;
import org.fi.uba.ar.ai.feedbacks.domain.FeedbackRepository;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.locations.domain.LocationArea;
import org.fi.uba.ar.ai.locations.domain.LocationRepository;
import org.fi.uba.ar.ai.services.domain.RatedService;
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

  private LocationRepository locationRepository;

  private FeedbackRepository feedbackRepository;

  public ServiceInteractor(ServiceRepository serviceRepository,
      ServiceCategoryRepository serviceCategoryRepository,
      ServiceSubCategoryRepository serviceSubCategoryRepository,
      UserRepository userRepository,
      LocationRepository locationRepository,
      FeedbackRepository feedbackRepository) {
    this.serviceRepository = serviceRepository;
    this.serviceCategoryRepository = serviceCategoryRepository;
    this.serviceSubCategoryRepository = serviceSubCategoryRepository;
    this.userRepository = userRepository;
    this.locationRepository = locationRepository;
    this.feedbackRepository = feedbackRepository;
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
    User provider = userRepository.findOne(request.getProviderId()).orElseThrow(
        () -> new EntityNotFoundException(
            "The provider with id " + request.getProviderId() + " doesn't exist."));
    Location location = locationRepository.findOne(request.getLocationId()).orElseThrow(
        () -> new EntityNotFoundException(
            "The location with id " + request.getLocationId() + " doesn't exist."));
    ServiceCategory category = this.getServiceCategory(request.getCategoryId());
    ServiceSubCategory subCategory = null;
    if (request.getSubCategoryId() != 0) {
      subCategory = serviceSubCategoryRepository.findOne(request.getSubCategoryId())
          .orElseThrow(() -> new EntityNotFoundException(
              "The sub category with id " + request.getSubCategoryId() + " doesn't exist."));
    }
    Service service = new Service(request.getName(), request.getDescription(), provider, location,
        category, subCategory, request.getStartTime(), request.getEndTime(),
        request.getStartDay(), request.getEndDay());
    return serviceRepository.save(service);
  }

  public Set<ServiceCategory> getServiceCategories() {
    return Sets.newHashSet(serviceCategoryRepository.findAll());
  }

  public ServiceCategory getServiceCategory(long categoryId) {
    return serviceCategoryRepository.findOne(categoryId).orElseThrow(
        () -> new EntityNotFoundException(
            "The category with id " + categoryId + " doesn't exist."));
  }

  public void delete(Service service) {
    serviceRepository.delete(service);
  }

  public List<ServiceCategory> findAllCategories() {
    return (List<ServiceCategory>) serviceCategoryRepository.findAll();
  }

  public ServiceCategory findCategoryByName(String categoryName) {
    return serviceCategoryRepository.findByName(categoryName);
  }

  public Service save(Service service) {
    return serviceRepository.save(service);
  }

  public List<Service> findAllByProviderId(long id) {
    List<Service> services = serviceRepository.findByProviderId(id);
    return services;
  }

  public List<Service> findAllByProviderMatchingName(User provider, String name) {
    String likeFilter = "%" + name.replace(" ", "%") + "%";
    List<Service> services = serviceRepository
        .findByProviderIdAndNameIgnoreCaseContaining(provider.getId(), "%" + likeFilter + "%");
    return services;
  }

  public List<Service> findAllMatchingName(String value) {
    String likeFilter = "%" + value.replace(" ", "%") + "%";
    List<Service> services = serviceRepository
        .findByNameIgnoreCaseContaining("%" + likeFilter + "%");
    return orderServicesByFeedbackRatingDesc(services);
  }

  private List<Service> orderServicesByFeedbackRatingDesc(List<Service> services) {
    return calculateRates(services).stream().map(ratedService -> ratedService.getService()).collect(
        Collectors.toList());
  }

  public List<RatedService> calculateRates(List<Service> services) {
    List<RatedService> ratedServices = services.stream()
        .map(service -> calculateRate(service, service.getProvider()))
        .collect(Collectors.toList());
    ratedServices.sort(Comparator.comparing(RatedService::getRating).reversed());
    return ratedServices;
  }

  public RatedService calculateRate(Service service, User user) {
    List<Feedback> feedbacks = feedbackRepository
        .findByContractServiceIdAndRecipientId(service.getId(), user.getId());
    float rating = 0f;
    for (Feedback feedback : feedbacks) {
      rating += feedback.getRating();
    }
    if (feedbacks.size() > 0) {
      rating = rating / feedbacks.size();
    }
    return new RatedService(service, rating);
  }

  public List<Service> searchBy(String searchName, List<LocationArea> searchAreas,
      List<String> searchCategories,
      Integer searchStartDay, Integer searchEndDays) {
    String likeFilter = "%" + searchName.replace(" ", "%") + "%";
    likeFilter = "%" + likeFilter + "%";
    List<Service> services = serviceRepository
        .findByNameIgnoreCaseContainingAndLocationAreaInAndCategoryNameInAndStartDayGreaterThanEqualAndEndDayLessThanEqual(
            likeFilter, searchAreas, searchCategories, searchStartDay, searchEndDays);
    return orderServicesByFeedbackRatingDesc(services);
  }
}
