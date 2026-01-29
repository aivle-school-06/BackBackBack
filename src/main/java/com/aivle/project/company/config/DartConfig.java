package com.aivle.project.company.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * DART 관련 설정 바인딩.
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(DartProperties.class)
public class DartConfig {
}
