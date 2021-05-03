package com.org.exception.handler;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.common.flogger.FluentLogger;
import com.google.common.flogger.StackSize;
import com.org.exception.BaseException;
import com.org.exception.ErrorCode;
import com.org.exception.ErrorData;
import com.org.model.ErrorResponse;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AddressBookExceptionHandler extends ResponseEntityExceptionHandler {

  private static final FluentLogger flogger = FluentLogger.forEnclosingClass();

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest request) {
    return handleError.apply(ex, request);
  }
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(
        "MethodArgumentNotValidException thrown");
    var errors =
        ErrorResponse.builder()
            .errors(
                ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(
                        fieldError ->
                            ErrorCode.INVALID_REQUEST_PARAMETER.getErrorData(
                                fieldError.getDefaultMessage()))
                    .collect(Collectors.toList()))
            .build();
    return handleErrorResponse.apply(errors, new HttpHeaders());
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(
        "message : %s", ex.getMessage());
    var cause = ex.getCause() == null ? null : ex.getCause();
    if (cause instanceof InvalidFormatException) {
      var errors =
          ErrorResponse.builder()
              .errors(
                  ((InvalidFormatException) cause)
                      .getPath()
                      .stream()
                      .map(
                          reference ->
                              ErrorCode.INVALID_FORMAT.getErrorData(reference.getFieldName()))
                      .collect(Collectors.toList()))
              .build();
      return handleErrorResponse.apply(errors, headers);
    }
    return handleErrorData.apply(ErrorCode.INVALID_REQUEST.getErrorData(ex.getMessage()), headers);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(ex.getMessage());
    StringBuilder builder = new StringBuilder();
    ex.getSupportedMediaTypes().forEach(t -> builder.append(t + ", "));
    return handleErrorData.apply(
        ErrorCode.MEDIA_TYPE_NOT_SUPPORTED.getErrorData(
            ex.getContentType(), builder.substring(0, builder.length() - 2)),
        new HttpHeaders());
  }

  @ExceptionHandler({ConstraintViolationException.class})
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {

    flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(
        "ConstraintViolationException thrown");

    var errors = new ErrorResponse();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      errors.add(ErrorCode.INVALID_REQUEST_HEADER.getErrorData(violation.getMessage()));
    }
    return handleErrorResponse.apply(errors, new HttpHeaders());
  }

  private BiFunction<ErrorData, HttpHeaders, ResponseEntity<Object>> handleErrorData =
      (errorData, httpHeaders) ->
          new ResponseEntity<>(
              ErrorResponse.builder().errors(List.of(errorData)).build(),
              httpHeaders,
              errorData.getHttpStatus());

  private BiFunction<ErrorResponse, HttpHeaders, ResponseEntity<Object>> handleErrorResponse =
      (err, httpHeaders) ->
          new ResponseEntity<>(
              err, httpHeaders, err.getErrors().stream().findFirst().get().getHttpStatus());

  private BiFunction<BaseException, WebRequest, ResponseEntity<ErrorResponse>> handleError =
      (ex, request) -> {
        ErrorData errorData = ex.getErrorData();
        flogger.atSevere().withStackTrace(StackSize.FULL).withCause(ex).log(
            "message : %s", ex.getMessage());
        return new ResponseEntity<>(
            ErrorResponse.builder().errors(List.of(errorData)).build(), errorData.getHttpStatus());
      };
}
