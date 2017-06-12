package org.fi.uba.ar.ai.quotations.usecase;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.fi.uba.ar.ai.quotations.domain.Quotation;
import org.fi.uba.ar.ai.quotations.domain.QuotationRepository;
import org.fi.uba.ar.ai.quotations.domain.QuotationStatus;
import org.fi.uba.ar.ai.services.domain.Service;
import org.fi.uba.ar.ai.users.domain.User;

public class QuotationInteractor {

  private QuotationRepository quotationRepository;

  private ContractInteractor contractInteractor;

  public QuotationInteractor(
      QuotationRepository quotationRepository,
      ContractInteractor contractInteractor) {
    this.quotationRepository = quotationRepository;
    this.contractInteractor = contractInteractor;
  }

  public Quotation create(String description, User client, Service service,
      LocalDateTime scheduledTime) {
    return quotationRepository
        .save(new Quotation(description, client, service, scheduledTime));
  }

  public List<Quotation> findByClient(User loggedUser) {
    return quotationRepository.findByClientIdAndStatus(loggedUser.getId(), QuotationStatus.CREATED);
  }

  public List<Quotation> findByProvider(User loggedUser) {
    return quotationRepository.findByServiceProviderId(loggedUser.getId());
  }

  public void decline(Quotation quotation) {
    quotation.setStatus(QuotationStatus.DECLINED);
    quotationRepository.save(quotation);
  }

  public void approve(Quotation quotation) {
    quotation.setStatus(QuotationStatus.APPROVED);
    List<Quotation> quotations = Arrays.asList(quotation);
    Contract contract = new Contract(quotation.getClient(), quotation.getService(),
        quotation.getScheduledTime(),
        quotations);
    contractInteractor.create(contract);
    quotationRepository.save(quotation);
  }

  public Quotation save(Quotation quotation) {
    return quotationRepository.save(quotation);
  }
}