package org.fi.uba.ar.ai.contracts.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.Validate;
import org.fi.uba.ar.ai.feedbacks.domain.Feedback;
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
  private User client;

  @ManyToOne(optional = false)
  @JoinColumn(name = "service_id")
  private Service service;

  @Column(name = "scheduled_time", nullable = false)
  private LocalDateTime scheduledTime;

  @Column(name = "creation_time", nullable = false)
  private LocalDateTime creationTime;

  @Column(name = "is_client_approved", nullable = false)
  private boolean isClientApproved;

  @Column(name = "is_provider_approved", nullable = false)
  private boolean isProviderApproved;

  @Setter
  @Enumerated(EnumType.STRING)
  private ContractStatus status;

  @OneToMany()
  @JoinColumn(name = "quotation_id", referencedColumnName = "id")
  private List<Quotation> quotations = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "contract")
  private List<Feedback> feedbacks = new ArrayList<>();

  public Contract(User client, Service service, LocalDateTime scheduledTime,
      List<Quotation> quotations) {
    this.client = client;
    this.service = service;
    this.scheduledTime = scheduledTime;
    this.status = ContractStatus.IN_PROGRESS;
    this.quotations = quotations;
    this.creationTime = LocalDateTime.now();
    this.isClientApproved = false;
    this.isProviderApproved = false;
  }

  public void clientApproved() {
    if (isClientApproved == false) {
      isClientApproved = true;
      if (isProviderApproved) {
        status = ContractStatus.COMPLETED;
      }
    }
  }

  public void providerApproved() {
    if (isProviderApproved == false) {
      isProviderApproved = true;
      if (isClientApproved) {
        status = ContractStatus.COMPLETED;
      }
    }
  }

  public void addFeedback(final Feedback feedback) {
    Validate.notNull(feedback, "The Feedback cannot be null");
    if (feedbackAlreadyGivenByUser(feedback.getSender())) {
      throw new IllegalArgumentException("Feedback already given!");
    }
    this.feedbacks.add(feedback);
  }

  public boolean feedbackAlreadyGivenByUser(final User user) {
    Validate.notNull(user, "The User cannot be null");
    return feedbacks.stream().anyMatch(feedback -> feedback.getSender().equals(user));
  }

  public boolean isCompleted() {
    return ContractStatus.COMPLETED.equals(this.status);
  }
}
