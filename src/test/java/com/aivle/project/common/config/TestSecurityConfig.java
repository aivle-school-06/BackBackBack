package com.aivle.project.common.config;

import com.aivle.project.auth.token.JwtKeyProvider;
import com.aivle.project.auth.token.JwtProperties;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * 테스트용 JWT 설정 (파일 시스템 접근 제거).
 */
@TestConfiguration
public class TestSecurityConfig {

	private static final KeyPair KEY_PAIR = generateKeyPair();

	@Bean
	@Primary
	public JwtKeyProvider testJwtKeyProvider(JwtProperties jwtProperties) {
		return new JwtKeyProvider(jwtProperties) {
			@Override
			public RSAPrivateKey loadPrivateKey() {
				return (RSAPrivateKey) KEY_PAIR.getPrivate();
			}

			@Override
			public RSAPublicKey loadPublicKey() {
				return (RSAPublicKey) KEY_PAIR.getPublic();
			}
		};
	}

	private static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			return generator.generateKeyPair();
		} catch (Exception ex) {
			throw new IllegalStateException("테스트용 RSA 키 생성에 실패했습니다", ex);
		}
	}
}
