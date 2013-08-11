package kr.spring.batch.chapter08.test.retry;

import kr.spring.batch.chapter08.test.AbstractRobustnessTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter08.test.retry.RetryBehaviorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 12. 오전 1:02
 */
@ContextConfiguration(classes = { RetryBehaviorConfiguration.class })
public class RetryBehaviorTest extends AbstractRobustnessTest {

	@Autowired
	Job retryPolicyJob;

	@Autowired
	RetryListener mockRetryListener;

	@Before
	public void init() {
		Mockito.reset(mockRetryListener);
		Mockito.when(mockRetryListener.open(Mockito.any(RetryContext.class), Mockito.any(RetryCallback.class)))
		       .thenReturn(true);
	}

	@Test
	public void sunnyDay() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		JobExecution exec =
				jobLauncher.run(job,
				                new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				                                          .toJobParameters());

		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertRead(read, exec);
		assertWrite(read, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);    // 5, 5, 2
		assertRollback(0, exec);
	}
}
