package org.fi.uba.ar.ai.locations.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "location")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Getter
public class Location {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @Column(name = "area")
  @Enumerated(EnumType.STRING)
  private LocationArea area;

  private Coordinate coordinate;

  public Location(String name, LocationArea area) {
    this(name, area, new Coordinate());
  }

  public Location(final String name, final LocationArea area, final Coordinate coordinate) {
    this.name = name;
    this.area = area;
    this.coordinate = coordinate;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Location location = (Location) o;

    return new EqualsBuilder().append(id, location.id).append(name, location.name).append(area,
        location.area).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(area).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).append("area", area)
        .toString();
  }
}
