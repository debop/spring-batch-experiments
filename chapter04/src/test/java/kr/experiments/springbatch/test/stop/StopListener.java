package kr.experiments.springbatch.test.stop;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

/**
 * kr.experiments.springbatch.test.stop.StopListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 5:52
 */
@Slf4j
@Component("stopListener")
public class StopListener {

    private StepExecution stepExecution;

    @Setter private boolean stop = false;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @AfterRead
    public void afterRead() {
        if (stopConditionMet()) {
            log.info("읽기 작업 후에 중단 여부를 판단했습니다. 중단을 요청합니다.");
            stepExecution.setTerminateOnly();
        }
    }

    private boolean stopConditionMet() {
        return stop;
    }
}
