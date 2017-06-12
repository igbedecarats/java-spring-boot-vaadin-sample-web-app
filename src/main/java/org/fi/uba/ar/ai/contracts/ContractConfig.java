package org.fi.uba.ar.ai.contracts;

import org.fi.uba.ar.ai.contracts.domain.ContractRepository;
import org.fi.uba.ar.ai.contracts.usecase.ContractInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContractConfig {

  @Autowired
  private ContractRepository contractRepository;

  @Bean
  public ContractInteractor contractInteractor() {
    return new ContractInteractor(contractRepository);
  }
}
