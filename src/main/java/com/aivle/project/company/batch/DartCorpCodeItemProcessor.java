package com.aivle.project.company.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * DART 기업 코드 Processor 스캐폴딩.
 */
@Component
public class DartCorpCodeItemProcessor implements ItemProcessor<DartCorpCodeItem, DartCorpCodeItem> {

	@Override
	public DartCorpCodeItem process(DartCorpCodeItem item) {
		return item;
	}
}
