package kr.spring.batch.chapter10.tasklet;

import kr.spring.batch.chapter10.batch.BatchService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter10.tasklet.GenerateReportTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:46
 */
public class GenerateReportTasklet extends AbstractBatchServiceTasklet {

    public GenerateReportTasklet() {}

    public GenerateReportTasklet(BatchService batchService) {
        super(batchService);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        getBatchService().generateReport(chunkContext.getStepContext().getStepExecution().getJobExecution());
        return RepeatStatus.FINISHED;
    }
}
