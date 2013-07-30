package kr.experiments.springbatch.chapter02.structure;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.experiments.springbatch.chapter02.structure.DummyTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 12:09
 */
public class DummyTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        return RepeatStatus.FINISHED
    }
}
