package org.fi.uba.ar.ai.ui.views.client;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.quotations.domain.Quotation;

@AllArgsConstructor
@Getter
public class QuotationModifiedEvent implements Serializable {

  private Quotation quotation;

}
