package org.fi.uba.ar.ai.contracts.usecase;

import java.util.List;
import org.fi.uba.ar.ai.contracts.domain.Contract;
import org.fi.uba.ar.ai.contracts.domain.ContractRepository;
import org.fi.uba.ar.ai.users.domain.User;

public class ContractInteractor {

  private ContractRepository contractRepository;

  public ContractInteractor(ContractRepository contractRepository) {
    this.contractRepository = contractRepository;
  }

  public Contract save(Contract contract) {
    return contractRepository.save(contract);
  }

  public List<Contract> findForUser(User loggedUser) {
    return contractRepository.findByClientIdOrServiceProviderId(loggedUser.getId(), loggedUser.getId());
  }

  public void markAsDoneByUser(final Contract contract, final User user) {
    if (user.equals(contract.getClient())) {
      contract.clientApproved();
    } else if (user.equals(contract.getService().getProvider())) {
      contract.providerApproved();
    } else {
      throw new IllegalArgumentException("User doesn't belong to contract");
    }
    save(contract);
  }
}
