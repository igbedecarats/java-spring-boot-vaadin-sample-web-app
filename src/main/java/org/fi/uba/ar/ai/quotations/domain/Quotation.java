package org.fi.uba.ar.ai.quotations.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;

@Entity
@Table(name = "quotation")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Quotation {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "description", nullable = false)
  @Setter
  private String description;

  @ManyToOne(optional = false)
  @JoinColumn(name = "client_id")
  @Setter
  private User client;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_id")
  @Setter
  private Service service;

  @Column(name = "scheduled_time", nullable = false)
  @Setter
  private LocalDateTime scheduledTime;

  @Column(name = "creation_time", nullable = false)
  private LocalDateTime creationTime;

  @Setter
  @Enumerated(EnumType.STRING)
  private QuotationStatus status;

  public Quotation(String description, User client, Service service,
      LocalDateTime scheduledTime) {
    this.description = description;
    this.client = client;
    this.service = service;
    this.scheduledTime = scheduledTime;
    this.creationTime = LocalDateTime.now();
    this.status = QuotationStatus.CREATED;
  }

  public boolean isCreated() {
    return status.equals(QuotationStatus.CREATED);
  }

}
