package org.fi.uba.ar.ai.locations.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Coordinate {

  @Column(name = "latitude", nullable = false)
  @Setter
  private double latitude;

  @Column(name = "longitude", nullable = false)
  @Setter
  private double longitude;

}
