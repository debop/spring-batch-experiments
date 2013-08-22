package kr.spring.batch.chapter02.test.structure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter02.test.structure.DummyTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 12:09
 */
@Slf4j
public class DummyTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("Execute Tasklet... StepName=[{}]", chunkContext.getStepContext().getStepName());
        return RepeatStatus.FINISHED;
    }
}
