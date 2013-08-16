package kr.spring.batch.chapter10.decider;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * kr.spring.batch.chapter10.decider.SkippedItemsDecider
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:26
 */
public class SkippedItemsDecider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (!stepExecution.getExitStatus().equals(ExitStatus.FAILED) &&
            stepExecution.getSkipCount() > 0) {
            return new FlowExecutionStatus("COMPLETED WITH SKIPS");
        } else {
            return new FlowExecutionStatus(jobExecution.getExitStatus().getExitCode());
        }
    }
}
