package kr.spring.batch.chapter04.test.stop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter04.test.stop.StopTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 5:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { StopConfiguration.class })
public class StopTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job readWriteJob;

	@Autowired
	private Job taskletJob;

	@Autowired
	private Job jobOperatorJob;

	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private StopListener stopListener;

	@Autowired
	private ProcessItemsTasklet tasklet;

	/**
	 * Listener 에게 Job 중단을 요청합니다.
	 *
	 * @throws Exception
	 */
	@Test
	public void stopReadWrite() throws Exception {
		JobExecution jobExecution = jobLauncher.run(readWriteJob, new JobParameters());
		assertThat(jobExecution.getStatus()).isIn(BatchStatus.STARTING, BatchStatus.STARTED);
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(readWriteJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(1);
		stopListener.setStop(true);

		waitForTermination(readWriteJob);
		runningExecutions = jobOperator.getRunningExecutions(readWriteJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(0);
	}

	/**
	 * Tasklet 에게 Job 중단을 요청합니다.
	 *
	 * @throws Exception
	 */
	@Test
	public void stopTasklet() throws Exception {
		JobExecution jobExecution = jobLauncher.run(taskletJob, new JobParameters());
		assertThat(jobExecution.getStatus()).isIn(BatchStatus.STARTING, BatchStatus.STARTED);
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(taskletJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(1);
		tasklet.setStop(true);

		waitForTermination(taskletJob);
		runningExecutions = jobOperator.getRunningExecutions(taskletJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(0);
	}

	/**
	 * JobOperator 가 Job 중단을 수행합니다.
	 *
	 * @throws Exception
	 */
	@Test
	public void stopWithJobOperator() throws Exception {
		JobExecution jobExecution = jobLauncher.run(jobOperatorJob, new JobParameters());
		assertThat(jobExecution.getStatus()).isIn(BatchStatus.STARTING, BatchStatus.STARTED);
		Set<Long> runningExecutions = jobOperator.getRunningExecutions(jobOperatorJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(1);

		Long executionId = runningExecutions.iterator().next();
		boolean stopMessageSent = jobOperator.stop(executionId);
		assertThat(stopMessageSent).isTrue();

		waitForTermination(jobOperatorJob);
		runningExecutions = jobOperator.getRunningExecutions(jobOperatorJob.getName());
		assertThat(runningExecutions.size()).isEqualTo(0);
	}

	private void waitForTermination(Job job) throws NoSuchJobException,
			InterruptedException {
		int timeout = 10000;
		int current = 0;

		while (jobOperator.getRunningExecutions(job.getName()).size() > 0 && current < timeout) {
			Thread.sleep(100);
			current += 100;
		}

		if (jobOperator.getRunningExecutions(job.getName()).size() > 0) {
			throw new IllegalStateException("the execution hasn't stopped " +
					                                "in the expected period (timeout = " + timeout + " ms)." +
					                                "Consider increasing the timeout before checking if it's a bug.");
		}
	}
}
