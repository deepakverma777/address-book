package com.org.exception.handler;

import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.org.exception.ErrorCode;
import com.org.model.ErrorResponse;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GenericExceptionHandler {

  private static final FluentLogger flogger = FluentLogger.forEnclosingClass();

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
    flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(
        "%s Exception thrown", ex.getMessage());
    var errorData = ErrorCode.INTERNAL_SERVER_ERROR.getErrorData();
    var errors = List.of(errorData);
    return new ResponseEntity<>(
        ErrorResponse.builder().errors(errors).build(), errorData.getHttpStatus());
  }
}
