package org.fi.uba.ar.ai.services.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.services.domain.ServiceSubCategory;

@AllArgsConstructor
@Getter
public class SubCategoryDto {

  private long id;
  private String name;
  private long categoryId;

  public static SubCategoryDto toDto(final ServiceSubCategory subCategory) {
    return new SubCategoryDto(subCategory.getId(), subCategory.getName(),
        subCategory.getCategory().getId());
  }

}
