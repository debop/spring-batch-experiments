package kr.spring.batch.chapter10.tasklet;

import kr.spring.batch.chapter10.batch.BatchService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;

/**
 * kr.spring.batch.chapter10.tasklet.AbstractBatchServiceTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:18
 */
public abstract class AbstractBatchServiceTasklet implements Tasklet {

	public AbstractBatchServiceTasklet() {}

	public AbstractBatchServiceTasklet(BatchService batchService) {
		this.batchService = batchService;
	}

	@Getter
	@Setter
	private BatchService batchService;

	public ExecutionContext getJobExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
	}

	public ExecutionContext getStepExecutionContext(ChunkContext chunkContext) {
		return chunkContext.getStepContext().getStepExecution().getExecutionContext();
	}
}
