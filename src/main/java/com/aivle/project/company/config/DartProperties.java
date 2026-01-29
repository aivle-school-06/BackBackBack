package com.aivle.project.company.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * DART API 및 기업 목록 동기화 설정.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dart")
public class DartProperties {

	/**
	 * DART API 인증키 (환경변수 DART_API_KEY).
	 */
	private String apiKey;

	private final CorpSync corpSync = new CorpSync();

	@Getter
	@Setter
	public static class CorpSync {

		/**
		 * 배치 처리 청크 크기.
		 */
		private int chunkSize = 1000;

		private final Schedule schedule = new Schedule();
	}

	@Getter
	@Setter
	public static class Schedule {

		/**
		 * 스케줄러 활성화 여부 (기본 OFF).
		 */
		private boolean enabled = false;

		/**
		 * 크론 표현식 (기본: 매일 03:00 KST).
		 */
		private String cron = "0 0 3 * * *";
	}
}
