package com.aivle.project.company.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

/**
 * DART 기업 코드 Writer 스캐폴딩.
 */
@Component
public class DartCorpCodeItemWriter implements ItemWriter<DartCorpCodeItem> {

	@Override
	public void write(Chunk<? extends DartCorpCodeItem> items) {
		// 스캐폴딩: Step3에서 실제 업서트 로직을 구현합니다.
	}
}
