package org.fi.uba.ar.ai.contracts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;

@Entity
@Table(name = "contract")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Contract {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "client_id")
  @Setter
  private User client;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_id")
  @Setter
  private Service service;

  @Column(name = "scheduled_time", nullable = false)
  private LocalDateTime scheduledTime;

  @Column(name = "creation_time", nullable = false)
  private LocalDateTime creationTime;

  @Setter
  @Enumerated(EnumType.STRING)
  private ContractStatus status;

  @OneToMany()
  @JoinColumn(name="quotation_id", referencedColumnName="id")
  private List<Quotation> quotations = new ArrayList<>();

  public Contract(User client, Service service, LocalDateTime scheduledTime,
      List<Quotation> quotations) {
    this.client = client;
    this.service = service;
    this.scheduledTime = scheduledTime;
    this.status = ContractStatus.CREATED;
    this.quotations = quotations;
    this.creationTime = LocalDateTime.now();
  }
}
