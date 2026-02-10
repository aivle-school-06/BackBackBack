package com.aivle.project.common.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class SecurityEndpointAuthorizationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("공개 검색 API는 인증 없이 접근 가능하다")
	void publicSearch_shouldPermitAll() throws Exception {
		mockMvc.perform(get("/api/companies/search").param("keyword", "테스트"))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("내 기업 조회는 인증이 없으면 401이다")
	void companiesMe_shouldRequireAuthentication() throws Exception {
		mockMvc.perform(get("/api/companies/me"))
			.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("내 기업 조회는 ROLE_USER면 접근 가능하다")
	void companiesMe_shouldAllowRoleUser() throws Exception {
		mockMvc.perform(get("/api/companies/me")
				.with(jwt().jwt(token -> token.claim("userId", 1L))
					.authorities(new SimpleGrantedAuthority("ROLE_USER"))))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("신규 워치리스트 지표 경로는 ROLE_USER 요청 시 인증/인가에서 차단되지 않는다")
	void watchlistMetricValues_shouldNotFailByAuthorization() throws Exception {
		mockMvc.perform(get("/api/watchlists/metrics/values")
				.param("year", "2026")
				.param("quarter", "1")
				.with(jwt().jwt(token -> token.claim("userId", 1L))
					.authorities(new SimpleGrantedAuthority("ROLE_USER"))))
			.andExpect(result -> {
				int status = result.getResponse().getStatus();
				assertThat(status).isNotEqualTo(401);
				assertThat(status).isNotEqualTo(403);
			});
	}

	@Test
	@DisplayName("신규 파일 다운로드 URL 경로는 ROLE_USER 요청 시 인증/인가에서 차단되지 않는다")
	void fileDownloadUrl_shouldNotFailByAuthorization() throws Exception {
		mockMvc.perform(get("/api/files/1/download-url")
				.with(jwt().jwt(token -> token.claim("userId", 1L))
					.authorities(new SimpleGrantedAuthority("ROLE_USER"))))
			.andExpect(result -> {
				int status = result.getResponse().getStatus();
				assertThat(status).isNotEqualTo(401);
				assertThat(status).isNotEqualTo(403);
			});
	}

	@Test
	@DisplayName("신규 AI 리포트 생성 경로는 ROLE_USER 요청 시 인증/인가에서 차단되지 않는다")
	void aiReports_shouldNotFailByAuthorization() throws Exception {
		mockMvc.perform(post("/api/companies/1/ai-reports")
				.with(jwt().jwt(token -> token.claim("userId", 1L))
					.authorities(new SimpleGrantedAuthority("ROLE_USER"))))
			.andExpect(result -> {
				int status = result.getResponse().getStatus();
				assertThat(status).isNotEqualTo(401);
				assertThat(status).isNotEqualTo(403);
			});
	}

	@Test
	@DisplayName("ADMIN API는 ROLE_USER에게 403을 반환한다")
	void adminApi_shouldDenyRoleUser() throws Exception {
		mockMvc.perform(post("/api/admin/metric-averages/initialize")
				.with(jwt().jwt(token -> token.claim("userId", 1L))
					.authorities(new SimpleGrantedAuthority("ROLE_USER"))))
			.andExpect(status().isForbidden());
	}
}
