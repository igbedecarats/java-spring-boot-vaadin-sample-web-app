package org.fi.uba.ar.ai.ui.views.service;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.fi.uba.ar.ai.services.domain.Service;

@AllArgsConstructor
@Getter
public class ServiceCreatedEvent implements Serializable {

  private Service service;
}
