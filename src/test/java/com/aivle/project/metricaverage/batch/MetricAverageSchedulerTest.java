package com.aivle.project.metricaverage.batch;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aivle.project.metricaverage.service.MetricAverageBatchSaveResult;
import com.aivle.project.metricaverage.service.MetricAverageBatchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetricAverageSchedulerTest {

	@Mock
	private MetricAverageBatchService metricAverageBatchService;

	@InjectMocks
	private MetricAverageScheduler metricAverageScheduler;

	@Test
	@DisplayName("스케줄러가 전체 분기 저장 서비스를 호출한다")
	void saveMissingMetricAveragesDaily_shouldCallBatchService() {
		// given
		when(metricAverageBatchService.calculateAndInsertMissingAllQuarters())
			.thenReturn(new MetricAverageBatchSaveResult(1, 2, 3));

		// when
		metricAverageScheduler.saveMissingMetricAveragesDaily();

		// then
		verify(metricAverageBatchService, times(1)).calculateAndInsertMissingAllQuarters();
	}
}
