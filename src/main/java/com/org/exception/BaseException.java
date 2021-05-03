package com.org.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
  /** default serial version Id */
  private static final long serialVersionUID = 1L;
  // Encapsulated error data object
  private ErrorData errorData;

  public BaseException(ErrorData errorData) {
    super(errorData.getMessage());
    this.errorData = errorData;
  }

  public BaseException(ErrorData errorData, Throwable cause) {
    super(errorData.getMessage(), cause);
    this.errorData = errorData;
  }
}
