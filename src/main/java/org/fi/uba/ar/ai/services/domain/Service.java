package org.fi.uba.ar.ai.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "service")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Service {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", nullable = false)
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id")
  private ServiceCategory category;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "service_sub_categories",
      joinColumns = {@JoinColumn(name = "service_id", referencedColumnName = "id")},
      inverseJoinColumns = {
          @JoinColumn(name = "sub_category_id", referencedColumnName = "id", unique = true)}
  )
  private Set<ServiceSubCategory> subCategories = new LinkedHashSet<>();

  public Service(String name, String description, ServiceCategory category,
      Set<ServiceSubCategory> subCategories) {
    this.name = name;
    this.description = description;
    this.category = category;
    this.subCategories = subCategories;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Service service = (Service) o;

    return new EqualsBuilder().append(id, service.id).append(name, service.name)
        .append(description, service.description).append(category, service.category).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(name).append(description).append(category)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("name", name)
        .append("description", description).append("category", category).toString();
  }

}
