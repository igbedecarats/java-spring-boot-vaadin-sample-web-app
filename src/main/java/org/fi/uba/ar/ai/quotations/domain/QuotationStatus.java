package org.fi.uba.ar.ai.quotations.domain;

import lombok.Getter;

public enum QuotationStatus {

  CREATED("Enviado"), APPROVED("Aprovado"), DECLINED("Rechazado");

  @Getter
  private final String value;

  QuotationStatus(String value) {
    this.value = value;
  }
}
