package org.fi.uba.ar.ai.services.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.web.UserDto;

@AllArgsConstructor
@Getter
public class ServiceDto {

  private long id;
  private UserDto provider;
  private String name;
  private String description;
  private CategoryDto category;
  private SubCategoryDto subCategory;
  private Location location;
  private String startTime;
  private String endTime;
  private Integer startDay;
  private Integer endDay;
  private String localizedStartDay;
  private String localizedEndDay;

  public static ServiceDto toDto(final Service service) {
    return new ServiceDto(service.getId(), UserDto.toDto(service.getProvider()), service.getName(),
        service.getDescription(), CategoryDto.toDto(service.getCategory()),
        SubCategoryDto.toDto(service.getSubCategory()), service.getLocation(),
        service.getStartTime(), service.getEndTime(), service.getStartDay(), service.getEndDay(),
        service.getLocalizedStartDay(), service.getLocalizedEndDay());
  }
}
