package com.aivle.project.report.service;

import com.aivle.project.quarter.support.QuarterCalculator;
import com.aivle.project.report.dto.ReportMetricRowDto;
import com.aivle.project.report.dto.ReportMetricRowProjection;
import com.aivle.project.report.repository.CompanyReportMetricValuesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 보고서 지표 조회 서비스.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyReportMetricQueryService {

	private final CompanyReportMetricValuesRepository companyReportMetricValuesRepository;

	public List<ReportMetricRowDto> fetchLatestMetrics(String stockCode, int fromQuarterKey, int toQuarterKey) {
		String normalizedStockCode = normalizeStockCode(stockCode);
		if (normalizedStockCode.isBlank()) {
			throw new IllegalArgumentException("stockCode가 비어 있습니다.");
		}
		validateQuarterKey(fromQuarterKey);
		validateQuarterKey(toQuarterKey);
		if (fromQuarterKey > toQuarterKey) {
			throw new IllegalArgumentException("fromQuarterKey가 toQuarterKey보다 클 수 없습니다.");
		}

		List<ReportMetricRowProjection> rows = companyReportMetricValuesRepository
			.findLatestMetricsByStockCodeAndQuarterRange(normalizedStockCode, fromQuarterKey, toQuarterKey);
		log.info(
			"보고서 지표 조회 완료: stockCode={}, fromQuarterKey={}, toQuarterKey={}, count={}",
			normalizedStockCode,
			fromQuarterKey,
			toQuarterKey,
			rows.size()
		);
		return rows.stream()
			.map(ReportMetricRowDto::from)
			.toList();
	}

	private void validateQuarterKey(int quarterKey) {
		QuarterCalculator.parseQuarterKey(quarterKey);
	}

	private String normalizeStockCode(String stockCode) {
		if (stockCode == null) {
			return "";
		}
		String trimmed = stockCode.trim();
		if (trimmed.isBlank()) {
			return "";
		}
		if (trimmed.length() < 6) {
			return "0".repeat(6 - trimmed.length()) + trimmed;
		}
		return trimmed;
	}
}
