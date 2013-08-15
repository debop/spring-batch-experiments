package kr.spring.batch.chapter09.test.transaction;

import kr.spring.batch.chapter09.test.AbstractRobustnessTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.*;

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
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);

		JobExecution exec = jobLauncher.run(job,
		                                    new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                              .toJobParameters());

		assertThat(exec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
		assertRead(read, exec);
		assertWrite(read - 1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(6, exec);

		// rollback on first error, new attempt on the chunk,
		// tries item per item, rollback for single item on error
		assertRollback(2, exec);
	}

	@Test
	public void noRollbackOnException() throws Exception {
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
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);

		JobExecution exec = jobLauncher.run(
				noRollbackJob,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);

		assertThat(exec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
		assertRead(read, exec);
		assertWrite(read - 1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(7, exec);

		// no rollback on first error, new attempt on the chunk,
		// tries item per item, rollback for single item on error
		assertRollback(1, exec);
	}

	@Test
	public void notTransactionalReader() throws Exception {
		int read = 12;
		when(service.reading())
				.thenReturn("1")
				.thenReturn("2")
				.thenReturn("3")
				.thenReturn("4")
				.thenReturn("5")
				.thenReturn("6")
				.thenReturn("7")
				.thenReturn("8")
				.thenReturn("9")
				.thenReturn("10")
				.thenReturn("11")
				.thenReturn("12")
				.thenReturn(null);

		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {
				return !toFailWriting.equals(input);
			}

			@Override
			public void describeTo(Description description) {}
		}));
		doThrow(new ValidationException("")).when(service).writing(toFailWriting);

		JobExecution exec = jobLauncher.run(
				notTransactionalReaderJob,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);

		assertThat(exec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
		verify(service, times(13)).reading();
		assertRead(read, exec);
		assertWrite(read - 1, exec);
	}

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private QueueViewMBean productQueueView;

	@Test
	//@Ignore("예제는 제대로 되는데... 왜 productQueueView 작업을 수행하려면 예외가 발생한다. xml 로 만들었는데도 그렇다.")
	public void transactionalReader() throws Exception {

		while (jmsTemplate.receive() != null) {}

		int read = 12;
		for (int i = 1; i <= read; i++) {
			jmsTemplate.convertAndSend(String.valueOf(i));
		}
		assertThat(productQueueView.getQueueSize()).isEqualTo(read);

		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object input) {
				return !toFailWriting.equals(input);
			}

			@Override
			public void describeTo(Description description) {}
		}));
		doThrow(new DeadlockLoserDataAccessException("", null)).when(service).writing(toFailWriting);

		JobExecution exec = jobLauncher.run(
				transactionalReaderJob,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters()
		);
		assertThat(exec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());

		int expectedWritten = 5;
		int stillOnQueue = (int) productQueueView.getQueueSize();
		assertRead(10, exec);
		assertThat(stillOnQueue).isEqualTo(read - expectedWritten);
		assertWrite(expectedWritten, exec);
	}
}
