package org.fi.uba.ar.ai.locations.domain;

import java.util.Arrays;
import lombok.Getter;

public enum LocationArea {

  CABA("Capital Federal"), GBA_SUR("GBA Sur"), GBA_NORTE("GBA Norte"), GBA_OESTE("GBA Oeste");

  @Getter
  private final String value;

  LocationArea(String value) {
    this.value = value;
  }

  public static LocationArea getByValue(final String theValue) {
    return Arrays.asList(LocationArea.values()).stream()
        .filter(locationArea -> locationArea.getValue().equals(theValue)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("No Area was found with " + theValue));
  }
}
