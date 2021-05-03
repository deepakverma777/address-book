package com.org.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ErrorData {

  @JsonIgnore private HttpStatus httpStatus;

  @JsonProperty("code")
  private String code;

  @JsonProperty("reasonCode")
  private ReasonCode reasonCode;

  @JsonProperty("message")
  private String message;
}
