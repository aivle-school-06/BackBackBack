package com.aivle.project.perf;

import com.aivle.project.company.entity.CompaniesEntity;
import com.aivle.project.company.repository.CompaniesRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * perf 프로파일 실행 시 벤치마크용 기본 데이터를 준비한다.
 */
@Slf4j
@Component
@Profile("perf")
@RequiredArgsConstructor
public class PerfDataInitializer implements ApplicationRunner {

	public static final String PERF_STOCK_CODE = "900001";
	private static final String PERF_CORP_CODE = "90000001";
	private static final String PERF_CORP_NAME = "PERF_MOCK_COMPANY";

	private final CompaniesRepository companiesRepository;

	@Override
	@Transactional
	public void run(ApplicationArguments args) {
		if (companiesRepository.findByStockCode(PERF_STOCK_CODE).isPresent()) {
			return;
		}

		companiesRepository.save(CompaniesEntity.create(
			PERF_CORP_CODE,
			PERF_CORP_NAME,
			"PERF MOCK COMPANY",
			PERF_STOCK_CODE,
			LocalDate.now()
		));
		log.info("perf 기본 기업 데이터를 초기화했습니다. stockCode={}", PERF_STOCK_CODE);
	}
}
