package kr.spring.batch.chapter10.tasklet;

import kr.spring.batch.chapter10.batch.ImportMetadata;
import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.VerifyStoreInJobContextTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:32
 */
public class VerifyStoreInJobContextTasklet extends AbstractBatchServiceTasklet {

    @Setter
    private String outputDirectory;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().verify(outputDirectory);
        ImportMetadata importMetadata = getBatchService().extractMetadata(outputDirectory);
        getJobExecutionContext(chunkContext).putString("importId", importMetadata.getImportId());

        return RepeatStatus.FINISHED;
    }
}
