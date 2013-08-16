package kr.spring.batch.chapter10.tasklet;

import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.TrackImportTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:24
 */
public class TrackImportTasklet extends AbstractBatchServiceTasklet {

    @Setter private String importId;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().track(importId);
        return RepeatStatus.FINISHED;
    }
}
