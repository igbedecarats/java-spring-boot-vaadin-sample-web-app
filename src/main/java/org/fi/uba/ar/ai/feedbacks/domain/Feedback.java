package org.fi.uba.ar.ai.feedbacks.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
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
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.users.domain.User;

@Entity
@Table(name = "feedback")
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Feedback {

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "sender_id")
  @Setter
  private User sender;

  @ManyToOne(optional = false)
  @JoinColumn(name = "recipient_id")
  @Setter
  private User recipient;

  @ManyToOne(optional = false)
  @JoinColumn(name = "contract_id")
  @Setter
  private Contract contract;

  @Column(name = "creation_time", nullable = false)
  private LocalDateTime creationTime;

  @Column(name = "rating", nullable = false)
  private int rating;

  @Column(name = "comment", nullable = false)
  private int comment;

  public Feedback(User sender, User recipient, Contract contract, int rating, int comment) {
    this.sender = sender;
    this.recipient = recipient;
    this.contract = contract;
    this.creationTime = LocalDateTime.now();
    this.rating = rating;
    this.comment = comment;
  }
}
