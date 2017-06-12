package org.fi.uba.ar.ai.quotations.usecase;

import java.time.LocalDateTime;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.domain.QuotationRepository;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;

public class QuotationInteractor {

  private QuotationRepository quotationRepository;

  public QuotationInteractor(
      QuotationRepository quotationRepository) {
    this.quotationRepository = quotationRepository;
  }

  public Quotation create(String description, User client, Service service,
      LocalDateTime scheduledTime) {
    return quotationRepository
        .save(new Quotation(description, client, service, scheduledTime));
  }
}