package org.fi.uba.ar.ai.quotations.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

  List<Quotation> findByClientId(long id);

  List<Quotation> findByServiceProviderId(long id);
}
