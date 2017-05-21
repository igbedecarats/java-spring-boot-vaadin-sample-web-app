package org.fi.uba.ar.ai.locations.usecase;

import java.util.Optional;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.fi.uba.ar.ai.locations.domain.LocationArea;

@Getter
public class CreateLocationRequest {

  private String name;
  private LocationArea area;

  public CreateLocationRequest(final String name, final String area) {
    this.name = name;
    this.area = StringUtils.isNotBlank(area) ? LocationArea.valueOf(area) : null;
  }

}
