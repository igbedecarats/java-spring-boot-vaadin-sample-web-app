package org.fi.uba.ar.ai.locations.web;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateLocationHttpRequest {

  private String name;
  private String area;

}
