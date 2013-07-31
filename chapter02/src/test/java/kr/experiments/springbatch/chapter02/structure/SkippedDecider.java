package kr.experiments.springbatch.chapter02.structure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * 건너뛰기 할지 결정하는 결정자입니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 9:25
 */
@Slf4j
public class SkippedDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        FlowExecutionStatus status =
            stepExecution.getSkipCount() == 0
            ? FlowExecutionStatus.COMPLETED
            : new FlowExecutionStatus("SKIPPED");

        if(log.isTraceEnabled())
            log.trace("FlowExecutionStatus를 결정했습니다. FlowExecutionStatus=[{}]", status);

        return status;
    }
}
