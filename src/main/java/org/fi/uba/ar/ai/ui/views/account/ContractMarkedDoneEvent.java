package org.fi.uba.ar.ai.ui.views.account;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.contracts.domain.Contract;

@AllArgsConstructor
@Getter
public class ContractMarkedDoneEvent implements Serializable {

  private Contract contract;
}
