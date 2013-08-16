package kr.spring.batch.chapter10.decider;

import kr.spring.batch.chapter10.batch.BatchService;
import lombok.Setter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

/**
 * kr.spring.batch.chapter10.decider.FileExistsDecider
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:14
 */
public class FileExistsDecider implements JobExecutionDecider {

    @Setter private BatchService batchService;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        String targetFile = jobExecution.getJobParameters().getString("archiveFile");
        if (batchService.exists(targetFile)) {
            return new FlowExecutionStatus("FILE EXISTS");
        } else {
            return new FlowExecutionStatus("NO FILE");
        }
    }
}
