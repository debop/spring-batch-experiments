package kr.experiments.springbatch.test;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * kr.experiments.springbatch.test.CountDownTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 4:49
 */
@Slf4j
@Component
public class CountDownTasklet implements Tasklet {

	@Setter CountDownLatch countDownLatch;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		countDownLatch.countDown();
		return RepeatStatus.FINISHED;
	}
}
