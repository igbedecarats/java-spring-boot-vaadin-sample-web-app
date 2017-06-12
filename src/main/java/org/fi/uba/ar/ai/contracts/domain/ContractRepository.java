package org.fi.uba.ar.ai.contracts.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {

  List<Contract> findByClientIdOrServiceProviderId(long id, long id1);
}
