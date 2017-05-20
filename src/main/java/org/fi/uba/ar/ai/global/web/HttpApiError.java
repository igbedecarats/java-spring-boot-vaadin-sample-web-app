package org.fi.uba.ar.ai.global.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class HttpApiError {

  private HttpStatus status;

  @JsonInclude(Include.NON_EMPTY)
  private String message;
}