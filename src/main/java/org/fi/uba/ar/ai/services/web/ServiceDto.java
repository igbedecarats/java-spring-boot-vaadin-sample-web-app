package org.fi.uba.ar.ai.services.web;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
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
  private Set<SubCategoryDto> subCategoryIds = new LinkedHashSet<>();

  public static ServiceDto toDto(final Service service) {
    return new ServiceDto(service.getId(), UserDto.toDto(service.getProvider()), service.getName(),
        service.getDescription(), CategoryDto.toDto(service.getCategory()),
        service.getSubCategories().stream().map(SubCategoryDto::toDto)
            .collect(Collectors.toSet()));
  }
}
