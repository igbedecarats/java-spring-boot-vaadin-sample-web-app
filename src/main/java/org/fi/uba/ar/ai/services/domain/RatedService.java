package org.fi.uba.ar.ai.services.domain;

import lombok.Getter;

@Getter
public class RatedService extends Service {

  protected Service service;

  protected Float rating;

  public RatedService(Service service, float rating) {
    super(service.getName(), service.getDescription(), service.getProvider(), service.getLocation(),
        service.getCategory(), service.getSubCategory(), service.getStartTime(),
        service.getEndTime(), service.getStartDay(), service.getEndDay());
    this.service = service;
    this.rating = rating;
  }
}
