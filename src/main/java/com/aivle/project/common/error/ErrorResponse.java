package com.aivle.project.common.error;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * 공통 에러 응답 포맷.
 */
public record ErrorResponse(String code, String message, String timestamp, String path) {

	public static ErrorResponse of(String code, String message, String path) {
		String timestamp = OffsetDateTime.now(ZoneOffset.UTC).toString();
		return new ErrorResponse(code, message, timestamp, path);
	}
}
