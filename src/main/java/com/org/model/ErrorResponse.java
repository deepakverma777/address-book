package com.org.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.org.exception.ErrorData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ErrorResponse {

  @JsonProperty("errors")
  private List<ErrorData> errors;

  public ErrorResponse() {
    errors = new ArrayList<>();
  }

  public ErrorResponse add(ErrorData error) {
    errors.add(error);
    return this;
  }

  public ErrorResponse addAll(List<ErrorData> errors) {
    this.errors.addAll(errors);
    return this;
  }
}
