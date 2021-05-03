package com.org.exception;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum ErrorCode {
	// 400
	INVALID_REQUEST_PARAMETER(
			"ADDR_API_ERR_0001",
			ReasonCode.INVALID_PARAMETER,
			"InvalidParameter : {0}",
			HttpStatus.BAD_REQUEST),
	INVALID_REQUEST(
			"ADDR_API_ERR_0002", 
			ReasonCode.BAD_REQUEST, 
			"{0}", 
			HttpStatus.BAD_REQUEST),
	INVALID_FORMAT(
			"ADDR_API_ERR_0003",
			ReasonCode.PARSE_ERROR,
			"Invalid Format : {0}",
			HttpStatus.BAD_REQUEST),
	INVALID_REQUEST_HEADER(
			"ADDR_API_ERR_0004",
			ReasonCode.INVALID_HEADER,
			"Invalid request header : {0}",
			HttpStatus.BAD_REQUEST),    
	// 404
	NOT_FOUND(
			"ADDR_API_ERR_1001", 
			ReasonCode.RESOURCE_UNAVAILABLE, "{0} not found", 
			HttpStatus.NOT_FOUND),

	// 500
	INTERNAL_SERVER_ERROR(
			"ADDR_API_ERR_2001",
			ReasonCode.INTERNAL_SERVER_ERROR,
			"Error Occurred in  address book service",
			HttpStatus.INTERNAL_SERVER_ERROR),
	MEDIA_TYPE_NOT_SUPPORTED(
			"COMM_API_ERR_4002",
			ReasonCode.INVALID,
			"{0} media type is not supported. Supported media types are {1}",
			HttpStatus.UNSUPPORTED_MEDIA_TYPE);

	private final String code;

	private final ReasonCode reasonCode;

	private final String description;

	private final HttpStatus httpStatus;

	public ErrorData getErrorData() {
		return new ErrorData(this.httpStatus, this.code, this.reasonCode, this.description);
	}

	public ErrorData getErrorData(Object... v) {
		return new ErrorData(this.httpStatus, this.code, this.reasonCode, this.getDescription(v));
	}

	public String getDescription(Object... v) {
		return MessageFormat.format(description, v);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public static ErrorCode fromCode(String text) {
		for (ErrorCode b : ErrorCode.values()) {
			if (b.code.equalsIgnoreCase(text)) {
				return b;
			}
		}
		return null;
	}
}
