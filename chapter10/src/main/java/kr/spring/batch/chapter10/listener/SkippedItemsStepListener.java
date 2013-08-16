package kr.spring.batch.chapter10.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * kr.spring.batch.chapter10.listener.SkippedItemsStepListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:06
 */
@Slf4j
public class SkippedItemsStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // Step 수행 전
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (!stepExecution.getExitStatus().equals(ExitStatus.FAILED) && stepExecution.getSkipCount() > 0) {
            return new ExitStatus("COMPLETED WITH SKIPS");
        }
        return stepExecution.getExitStatus();
    }
}
