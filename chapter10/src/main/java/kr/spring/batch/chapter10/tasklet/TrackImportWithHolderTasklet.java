package kr.spring.batch.chapter10.tasklet;

import kr.spring.batch.chapter10.batch.ImportMetadataHolder;
import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.TrackImportWithHolderTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:27
 */
public class TrackImportWithHolderTasklet extends AbstractBatchServiceTasklet {

    @Setter ImportMetadataHolder importMetadataHolder;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().track(importMetadataHolder.get().getImportId());
        return RepeatStatus.FINISHED;
    }
}
