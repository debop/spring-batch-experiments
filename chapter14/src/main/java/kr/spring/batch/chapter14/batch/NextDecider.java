package kr.spring.batch.chapter14.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * kr.spring.batch.chapter14.batch.NextDecider
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:31
 */
public class NextDecider implements JobExecutionDecider {
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if (stepExecution.getWriteCount() > 0) {
			return new FlowExecutionStatus("NEXT");
		}
		return FlowExecutionStatus.COMPLETED;
	}
}
