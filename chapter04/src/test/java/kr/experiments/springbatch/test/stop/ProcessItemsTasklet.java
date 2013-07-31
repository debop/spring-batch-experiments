package kr.experiments.springbatch.test.stop;

import lombok.Setter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

/**
 * kr.experiments.springbatch.test.stop.ProcessItemTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 5:49
 */
@Component
public class ProcessItemsTasklet implements Tasklet {

    @Setter private boolean stop;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        if (shouldStop()) {
            // HINT: 종료 요청이 왔으므로 종료하도록 한다.
            chunkContext.getStepContext().getStepExecution().setTerminateOnly();
        }
        processItem();
        return moreItemsToProcess() ? RepeatStatus.CONTINUABLE : RepeatStatus.FINISHED;
    }

    private void processItem() {}

    private boolean moreItemsToProcess() {
        return true;
    }

    private boolean shouldStop() {
        return stop;
    }
}
