package com.aivle.project.report.repository;

import com.aivle.project.report.entity.CompanyReportMetricValuesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 보고서 지표 값 조회/저장 리포지토리.
 */
public interface CompanyReportMetricValuesRepository extends JpaRepository<CompanyReportMetricValuesEntity, Long> {
}
