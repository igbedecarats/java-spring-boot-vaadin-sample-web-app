package org.fi.uba.ar.ai.services.domain;

import lombok.Getter;

@Getter
public class RatedService extends Service {

  protected float rating;

  public RatedService(Service service, float rating) {
    super(service.getName(), service.getDescription(), service.getProvider(), service.getLocation(),
        service.getCategory(), service.getSubCategory(), service.getStartTime(),
        service.getEndTime(), service.getStartDay(), service.getEndDay());
    this.rating = rating;
  }
}
