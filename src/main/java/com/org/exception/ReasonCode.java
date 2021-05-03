package com.org.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReasonCode {
  BAD_REQUEST("badRequest"),
  INVALID_PARAMETER("InvalidParameter"),
  INVALID_QUERY("invalidQuery"),
  INVALID_HEADER("invalidHeader"),
  PARSE_ERROR("parseError"),
  TOO_MANY_PARTS("tooManyParts"),
  WRONG_URL_FOR_UPLOAD("wrongUrlForUpload"),
  NOT_MODIFIED("notModified"),
  UNAUTHORIZED("unauthorized"),
  AUTHERROR("authError"),
  EXPIRED("expired"),
  REQUIRED("required"),
  RESOURCE_UNAVAILABLE("resourceUnavailable"),
  SERVICE_UNAVAILABLE("serviceUnavailable"),
  INVALID("invalid"),
  INTERNAL_SERVER_ERROR("internalServerError");

  @JsonValue private final String reasonCode;
}
