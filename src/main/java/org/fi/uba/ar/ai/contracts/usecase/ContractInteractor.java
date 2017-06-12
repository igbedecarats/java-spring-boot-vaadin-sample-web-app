package org.fi.uba.ar.ai.contracts.usecase;

import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.domain.ContractRepository;

public class ContractInteractor {

  private ContractRepository contractRepository;

  public ContractInteractor(ContractRepository contractRepository) {
    this.contractRepository = contractRepository;
  }

  public Contract create(Contract contract) {
    return contractRepository.save(contract);
  }
}
