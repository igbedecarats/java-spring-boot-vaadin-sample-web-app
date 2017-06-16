package org.fi.uba.ar.ai.locations.domain;

import lombok.Getter;

public enum LocationArea {

  CABA("Capital Federal"), GBA_SUR("GBA Sur"), GBA_NORTE("GBA Norte"), GBA_OESTE("GBA Oeste");

  @Getter
  private final String value;

  LocationArea(String value) {
    this.value = value;
  }
}
