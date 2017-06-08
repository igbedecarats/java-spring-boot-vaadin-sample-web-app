package org.fi.uba.ar.ai.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "service_category")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class ServiceCategory {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "category")
  private Set<ServiceSubCategory> subCategories = new LinkedHashSet<>();

  public ServiceCategory(final String name) {
    this.name = name;
  }

  public void addSubCategories(final Set<ServiceSubCategory> subCategories) {
    Validate.notNull(subCategories, "The Sub Categories cannot be null");
    this.subCategories.addAll(subCategories);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ServiceCategory category = (ServiceCategory) o;

    return new EqualsBuilder().append(name, category.name).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(name).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name).toString();
  }

}
