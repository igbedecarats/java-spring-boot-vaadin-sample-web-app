package org.fi.uba.ar.ai.services;

import org.fi.uba.ar.ai.services.domain.ServiceCategoryRepository;
import org.fi.uba.ar.ai.services.domain.ServiceRepository;
import org.fi.uba.ar.ai.services.usecase.ServiceInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

  @Autowired
  private ServiceRepository serviceRepository;

  @Autowired
  private ServiceCategoryRepository serviceCategoryRepository;

  @Bean
  public ServiceInteractor serviceInteractor() {
    return new ServiceInteractor(serviceRepository, serviceCategoryRepository);
  }

}
