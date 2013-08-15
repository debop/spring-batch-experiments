package kr.spring.batch.chapter09.test.transaction;

import kr.spring.batch.chapter09.test.AbstractRobustnessTest;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * kr.spring.batch.chapter09.test.transaction.TransactionBehaviorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오후 3:20
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TransactionBehaviorConfiguration.class })
public class TransactionBehaviorTest extends AbstractRobustnessTest {

	@Autowired
	private Job job;

	@Autowired
	private Job noRollbackJob;

	@Autowired
	private Job notTransactionalReaderJob;

	@Autowired
	private Job transactionalReaderJob;

	@Test
	public void rollbackOnException() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);

		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object item) {
				return !toFailWriting.equals(item);
			}

			@Override
			public void describeTo(Description description) { }
		}));
		doThrow(new org.springframework.batch.item.validator.ValidationException("")).when(service).writing(toFailWriting);

		JobExecution exec = jobLauncher.run(job,
		                                    new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                              .toJobParameters());

		Assertions.assertThat(exec.getExitStatus().getExitCode())
		          .isEqualTo(ExitStatus.COMPLETED.getExitCode());
		assertRead(read, exec);
		assertWrite(read - 1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(6, exec);
	}
}
