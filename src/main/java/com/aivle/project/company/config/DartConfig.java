package com.aivle.project.company.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * DART 관련 설정 바인딩.
 */
@Configuration
@EnableConfigurationProperties(DartProperties.class)
public class DartConfig {
}
