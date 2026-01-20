package com.aivle.project.auth.exception;

import com.aivle.project.common.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 인증 관련 예외 응답 처리.
 */
@RestControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex, HttpServletRequest request) {
		AuthErrorCode errorCode = ex.getErrorCode();
		ErrorResponse response = ErrorResponse.of(errorCode.getCode(), errorCode.getMessage(), request.getRequestURI());
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}
}
