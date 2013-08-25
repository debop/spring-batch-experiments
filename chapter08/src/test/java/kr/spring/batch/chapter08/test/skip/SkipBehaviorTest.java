package kr.spring.batch.chapter08.test.skip;

import kr.spring.batch.chapter08.test.AbstractRobustnessBatchTest;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter08.test.skip.SkipBehaviorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 2:44
 */
@Slf4j
@ContextConfiguration(classes = { SkipBehaviorConfiguration.class })
public class SkipBehaviorTest extends AbstractRobustnessBatchTest {

	@Test
	public void sunnyDay() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);
		JobExecution exec = jobLauncher.run(
				job,
				new JobParametersBuilder().addLong("time", System.currentTimeMillis())
				                          .toJobParameters());
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertRead(read, exec);
		assertWrite(read, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(0, exec);
	}

	@Test
	public void execptionInReading() throws Exception {
		when(service.reading())
				.thenReturn("1")
				.thenReturn("2")
				.thenReturn("3")
				.thenReturn("4")
				.thenReturn("5")
				.thenReturn("6")
				.thenThrow(new FlatFileParseException("", ""))
				.thenReturn("8")
				.thenReturn("9")
				.thenReturn("10")
				.thenReturn("11")
				.thenReturn("12")
				.thenReturn(null);

		JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                 .toJobParameters();
		JobExecution exec = jobLauncher.run(job, params);

		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(service, times(13)).reading();
		verify(service, times(11)).processing(anyString());
		verify(service, times(11)).writing(anyString());
		verify(skipListener, times(1)).onSkipInRead(any(Throwable.class));

		assertRead(11, exec);
		assertWrite(11, exec);
		assertReadSkip(1, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(0, exec);
	}

	@Test
	public void exceptionInWriting() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);

		final String toFailWriting = "7";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object item) {
				return !toFailWriting.equals(item);
			}

			@Override
			public void describeTo(Description description) {}
		}));

		doThrow(new DataIntegrityViolationException("")).when(service).writing(toFailWriting);

		JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                 .toJobParameters();
		JobExecution exec = jobLauncher.run(job, params);
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(service, times(13)).reading();
		verify(service, times(5 + 5 + 1 + 1 + 1 + 1 + 1 + 2)).processing(anyString());
		verify(service, times(5 + 2 + 1 + 1 + 3 + 2)).writing(anyString());
		assertRead(read, exec);
		assertWrite(read - 1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(1, exec);
		assertCommit(6, exec);
		assertRollback(2, exec);
	}

	@Test
	public void exceptionInProcessing() throws Exception {
		int read = 12;
		configureServiceForRead(service, read);

		final String toFailProcessing = "7";
		doNothing().when(service).processing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object item) {
				return !toFailProcessing.equals(item);
			}

			@Override
			public void describeTo(Description description) {}
		}));

		doThrow(new DataIntegrityViolationException("")).when(service).processing(toFailProcessing);
		doNothing().when(service).writing(anyString());

		JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                 .toJobParameters();
		JobExecution exec = jobLauncher.run(job, params);
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		verify(service, times(5 + 5 + 2 + 1)).reading();
		verify(service, times(5 + 1 + 1 + 4 + 2)).processing(anyString());
		verify(service, times(5 + 4 + 2)).writing(anyString());
		assertRead(read, exec);
		assertWrite(read - 1, exec);
		assertReadSkip(0, exec);
		assertProcessSkip(1, exec);
		assertWriteSkip(0, exec);
		assertCommit(3, exec);
		assertRollback(1, exec);
	}

	@Test
	public void exceptionInReadingThenWritingNoCallOnSkipListener() throws Exception {
		when(service.reading())
				.thenReturn("1")
				.thenReturn("2")
				.thenReturn("3")
				.thenReturn("4")
				.thenReturn("5")
				.thenReturn("6")
				.thenThrow(new FlatFileParseException("", ""))
				.thenReturn("8")
				.thenReturn("9")
				.thenReturn("10")
				.thenReturn("11")
				.thenReturn("12")
				.thenReturn(null);

		final String toFailWriting = "9";
		doNothing().when(service).writing(argThat(new BaseMatcher<String>() {
			@Override
			public boolean matches(Object item) {
				return !toFailWriting.equals(item);
			}

			@Override
			public void describeTo(Description description) {}
		}));

		doThrow(new RuntimeException("")).when(service).writing(toFailWriting);

		JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
		                                                 .toJobParameters();
		JobExecution exec = jobLauncher.run(job, params);
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);

		verify(service, times(11)).reading();
		verify(service, times(11)).processing(anyString());
		verify(service, times(8)).writing(anyString());

		// skip listener is not called, because transaction is rolled back
		// skip listener is called just before the commit!
		verify(skipListener, times(0)).onSkipInRead(any(Throwable.class));


		assertRead(10, exec);
		assertWrite(5, exec);
		assertReadSkip(1, exec);
		assertProcessSkip(0, exec);
		assertWriteSkip(0, exec);
		assertCommit(1, exec);
		assertRollback(2, exec);
	}
}
