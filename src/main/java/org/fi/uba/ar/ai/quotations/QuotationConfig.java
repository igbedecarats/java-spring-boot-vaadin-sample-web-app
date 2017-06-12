package org.fi.uba.ar.ai.quotations;

import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.fi.uba.ar.ai.quotations.domain.QuotationRepository;
import org.fi.uba.ar.ai.quotations.usecase.QuotationInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuotationConfig {

  @Autowired
  private QuotationRepository quotationRepository;

  @Autowired
  private ContractInteractor contractInteractor;

  @Bean
  public QuotationInteractor quotationInteractor() {
    return new QuotationInteractor(quotationRepository, contractInteractor);
  }

}
