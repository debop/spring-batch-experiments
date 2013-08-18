package kr.spring.batch.chapter14.batch;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * kr.spring.batch.chapter14.batch.CleanTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:30
 */
public class CleanTasklet implements Tasklet {
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		return RepeatStatus.FINISHED;
	}
}
