package kr.spring.batch.chapter10.tasklet;

import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.CleanTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 2:45
 */
public class CleanTasklet extends AbstractBatchServiceTasklet {

    @Setter String outputDirectory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().clean(outputDirectory);
        return RepeatStatus.FINISHED;
    }
}
