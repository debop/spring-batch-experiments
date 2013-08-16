package kr.spring.batch.chapter10.tasklet;

import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.DownloadTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:15
 */
public class DownloadTasklet extends AbstractBatchServiceTasklet {

    @Setter private String targetFile;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().download(targetFile);
        return RepeatStatus.FINISHED;
    }
}
