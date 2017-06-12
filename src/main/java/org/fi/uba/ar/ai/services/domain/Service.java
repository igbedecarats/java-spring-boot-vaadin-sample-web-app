package org.fi.uba.ar.ai.services.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.fi.uba.ar.ai.locations.domain.Location;
import org.fi.uba.ar.ai.users.domain.User;

@Entity
@Table(name = "service")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Service {

  @Id
  @GeneratedValue
  protected long id;

  @Column(name = "name", nullable = false)
  @Setter
  protected String name;

  @Column(name = "description", nullable = false)
  @Setter
  protected String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id")
  @Setter
  protected ServiceCategory category;

  @ManyToOne
  @JoinColumn(name = "sub_category_id")
  @Setter
  protected ServiceSubCategory subCategory;

  @ManyToOne(optional = false)
  @JoinColumn(name = "provider_id")
  @Setter
  protected User provider;

  @ManyToOne(optional = false)
  @JoinColumn(name = "location_id")
  @Setter
  protected Location location;

  @Column(name = "start_time")
  @Setter
  protected String startTime;

  @Column(name = "end_time")
  @Setter
  protected String endTime;

  @Column(name = "start_day")
  @Setter
  protected Integer startDay;

  @Column(name = "end_day")
  @Setter
  protected Integer endDay;

  public Service(String name, String description,
      ServiceCategory category, User provider, Location location) {
    this(name, description, provider, location, category, null, null, null, null, null);
  }

  public Service(String name, String description,
      User provider, Location location, ServiceCategory category,
      ServiceSubCategory subCategory, String startTime, String endTime,
      Integer startDay, Integer endDay) {
    Validate.notBlank(name, "The Service name cannot be blank.");
    Validate.notBlank(description, "The Service description cannot be blank.");
    Validate.notNull(provider, "The Service provider cannot be blank.");
    Validate.notNull(location, "The Service location cannot be blank.");
    Validate.notNull(category, "The Service category cannot be blank.");
    this.name = name;
    this.description = description;
    this.category = category;
    this.provider = provider;
    this.location = location;
    this.subCategory = subCategory;
    setTimesAndDays(startTime, endTime, startDay, endDay);
  }

  public void setTimesAndDays(String startTime, String endTime, Integer startDay,
      Integer endDay) {
    if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime) && startDay != null
        && endDay != null) {
      validateDayTimesRange(startTime, endTime);
      validateDaysRange(startDay, endDay);
      this.startTime = startTime;
      this.endTime = endTime;
      this.startDay = startDay;
      this.endDay = endDay;
    } else {
      this.startTime = null;
      this.endTime = null;
      this.startDay = null;
      this.endDay = null;
    }
  }

  public LocalTime getLocalStartTime() {
    return LocalTime.parse(startTime);
  }

  public LocalTime getLocalEndTime() {
    return LocalTime.parse(endTime);
  }

  public String getLocalizedStartDay() {
    return startDay != null ? getLocalizedDayOfTheWeek(startDay) : null;
  }

  public String getLocalizedEndDay() {
    return endDay != null ? getLocalizedDayOfTheWeek(endDay) : null;
  }

  public void setLocalizedStartDay(final String day) {
    this.startDay = getDayOfTheWeekFromLocalizedDay(day);
  }

  public void setLocalizedEndDay(final String day) {
    this.endDay = getDayOfTheWeekFromLocalizedDay(day);
  }

  private int getDayOfTheWeekFromLocalizedDay(String day) {
    return Arrays.stream(DayOfWeek.values()).filter(dayOfWeek ->
        StringUtils.capitalize(DayOfWeek.of(dayOfWeek.getValue())
            .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"))).equals(day)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid Day of the Week")).getValue();
  }

  private String getLocalizedDayOfTheWeek(final Integer dayOfTheWeek) {
    return StringUtils.capitalize(
        DayOfWeek.of(dayOfTheWeek).getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es")));
  }

  public static List<String> getLocalizedDaysOfTheWeek() {
    return Arrays.stream(DayOfWeek.values())
        .map(dayOfWeek -> StringUtils.capitalize(DayOfWeek.of(dayOfWeek.getValue())
            .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"))))
        .collect(Collectors.toList());
  }

  private void validateDaysRange(final Integer startDay, final Integer endDay) {
    try {
      DayOfWeek.of(startDay);
      DayOfWeek.of(endDay);
      Validate.isTrue(startDay <= endDay, "The Start Day must be less or equal to the End Day");
    } catch (DateTimeException e) {
      throw new IllegalArgumentException("Invalid Day of the Week", e);
    }
  }

  private void validateDayTimesRange(final String startTime, final String endTime) {
    try {
      LocalTime localStartTime = LocalTime.parse(startTime);
      LocalTime localEndTime = LocalTime.parse(endTime);
      if (!localStartTime.equals(localEndTime)) {
        Validate.isTrue(localStartTime.isBefore(localEndTime),
            "The Start Time must be earlier than the End Time.");
      }
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Invalid Time of the Day: " + e.getParsedString(), e);
    }
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

    return new EqualsBuilder().append(id, service.id).append(provider, service.provider)
        .append(name, service.name).append(description, service.description)
        .append(category, service.category).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37).append(id).append(provider).append(name).append(description)
        .append(category).toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("id", id).append("provider", provider)
        .append("name", name).append("description", description).append("category", category)
        .toString();
  }
}
