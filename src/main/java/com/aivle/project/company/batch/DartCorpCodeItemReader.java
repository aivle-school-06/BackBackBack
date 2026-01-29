package com.aivle.project.company.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

/**
 * DART 기업 코드 Reader 스캐폴딩.
 */
@Component
public class DartCorpCodeItemReader implements ItemReader<DartCorpCodeItem> {

	@Override
	public DartCorpCodeItem read() {
		return null;
	}
}
