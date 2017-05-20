package org.fi.uba.ar.ai.services.web;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.services.domain.ServiceCategory;

@AllArgsConstructor
@Getter
public class CategoryDto {

  private long id;
  private String name;
  private Set<SubCategoryDto> subCategories = new LinkedHashSet<>();

  public static CategoryDto toDto(final ServiceCategory category) {
    return new CategoryDto(category.getId(), category.getName(),
        category.getSubCategories().stream().map(SubCategoryDto::toDto)
            .collect(Collectors.toSet()));
  }

}
