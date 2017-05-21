package org.fi.uba.ar.ai.services.usecase;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateServiceRequest {

  private long providerId;
  private String name;
  private String description;
  private long locationId;
  private long categoryId;
  private List<Long> subCategoryIds = new ArrayList<>();

}
