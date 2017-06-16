package org.fi.uba.ar.ai.contracts.domain;

import lombok.Getter;

public enum ContractStatus {

  IN_PROGRESS("En Progreso"), COMPLETED("Completado"), DONE_BY_CLIENT(
      "Aprovado por el Cliente"), DONE_BY_PROVIDER("Aprovado por el Proveedor");

  @Getter
  private final String value;

  ContractStatus(String value) {
    this.value = value;
  }
}
