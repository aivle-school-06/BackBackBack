package com.aivle.project.company.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aivle.project.company.dto.AiAnalysisResponse;
import com.aivle.project.company.service.CompanyAiService;
import com.aivle.project.file.entity.FileUsageType;
import com.aivle.project.file.entity.FilesEntity;
import com.aivle.project.common.config.TestSecurityConfig;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class CompanyAiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyAiService companyAiService;

    @Test
    @DisplayName("ROLE_USER로 기업 AI 분석을 조회한다")
    void getCompanyAnalysis() throws Exception {
        // given
        AiAnalysisResponse response = new AiAnalysisResponse(
            "005930",
            "삼성전자",
            "2025Q4",
            Map.of("ROA", 1.23)
        );
        given(companyAiService.getCompanyAnalysis("005930", null, null)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/companies/005930/ai-analysis")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.company_code").value("005930"))
            .andExpect(jsonPath("$.data.company_name").value("삼성전자"))
            .andExpect(jsonPath("$.data.base_period").value("2025Q4"))
            .andExpect(jsonPath("$.data.predictions.ROA").value(1.23));
    }

    @Test
    @DisplayName("기업 AI 분석 조회는 인증이 없으면 401을 반환한다")
    void getCompanyAnalysis_unauthorized() throws Exception {
        // when & then
        mockMvc.perform(get("/api/companies/005930/ai-analysis"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("기업 AI 분석 조회는 ROLE_ADMIN도 접근 가능하다 (권한 계층)")
    void getCompanyAnalysis_allowedForAdmin() throws Exception {
        // given
        AiAnalysisResponse response = new AiAnalysisResponse(
            "005930",
            "삼성전자",
            "2025Q4",
            Map.of("ROA", 1.23)
        );
        given(companyAiService.getCompanyAnalysis("005930", null, null)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/companies/005930/ai-analysis")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("ROLE_ADMIN으로 기업 AI 리포트 PDF를 생성/저장한다 (연도/분기 미포함)")
    void generateCompanyAiReport() throws Exception {
        // given
        FilesEntity file = FilesEntity.create(
            FileUsageType.REPORT_PDF,
            "http://localhost/files/report_005930.pdf",
            "reports/report_005930.pdf",
            "report_005930.pdf",
            1024L,
            "application/pdf"
        );
        given(companyAiService.generateAndSaveReport("005930", null, null)).willReturn(file);

        // when & then
        mockMvc.perform(post("/api/companies/005930/ai-report")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.storageUrl").value("http://localhost/files/report_005930.pdf"));
    }

    @Test
    @DisplayName("ROLE_ADMIN으로 기업 AI 리포트 PDF를 생성/저장한다 (연도/분기 포함)")
    void generateCompanyAiReport_withYearAndQuarter() throws Exception {
        // given
        FilesEntity file = FilesEntity.create(
            FileUsageType.REPORT_PDF,
            "http://localhost/files/report_005930.pdf",
            "reports/report_005930.pdf",
            "report_005930.pdf",
            1024L,
            "application/pdf"
        );
        given(companyAiService.generateAndSaveReport("005930", 2026, 1)).willReturn(file);

        // when & then
        mockMvc.perform(post("/api/companies/005930/ai-report")
                .param("year", "2026")
                .param("quarter", "1")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("기업 AI 리포트 생성은 ROLE_USER만으로는 403을 반환한다")
    void generateCompanyAiReport_forbiddenForUserOnly() throws Exception {
        // when & then
        mockMvc.perform(post("/api/companies/005930/ai-report")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
            .andExpect(status().isForbidden());
    }
}
