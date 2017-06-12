package org.fi.uba.ar.ai.ui.views.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.contracts.domain.Contract;

@AllArgsConstructor
@Getter
public class ContractMarkedDoneEvent {

  private Contract contract;
}
