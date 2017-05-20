package org.fi.uba.ar.ai.services.web;

import java.util.Set;
import java.util.stream.Collectors;
import org.fi.uba.ar.ai.services.usecase.CreateServiceRequest;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/api/services")
public class ServiceController {

  @Autowired
  private ServiceInteractor interactor;

  @RequestMapping(value = "/{serviceId}", method = RequestMethod.GET)
  public ResponseEntity<ServiceDto> getServiceById(@PathVariable final long serviceId) {
    return ResponseEntity.ok(ServiceDto.toDto(interactor.find(serviceId)));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResponseEntity<ServiceDto> createService(@RequestBody final CreateServiceRequest request) {
    return ResponseEntity.ok(ServiceDto.toDto(interactor.create(request)));
  }

  @RequestMapping(value = "/categories", method = RequestMethod.GET)
  public ResponseEntity<Set<CategoryDto>> getCategories() {
    Set<CategoryDto> categories = interactor.getServiceCategories().stream().map(CategoryDto::toDto)
        .collect(Collectors.toSet());
    return ResponseEntity.ok(categories);
  }

  @RequestMapping(value = "/categories/{categoryId}", method = RequestMethod.GET)
  public ResponseEntity<CategoryDto> getCategoryById(@PathVariable final long categoryId) {
    return ResponseEntity.ok(CategoryDto.toDto(interactor.getServiceCategory(categoryId)));
  }
}
