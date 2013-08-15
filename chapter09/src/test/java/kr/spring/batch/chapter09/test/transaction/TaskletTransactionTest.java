package kr.spring.batch.chapter09.test.transaction;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * kr.spring.batch.chapter09.test.transaction.TaskletTransactionTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오후 1:40
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TaskletTransactionConfiguration.class })
public class TaskletTransactionTest {

	@Autowired
	private Job noTransactionJob;

	@Autowired
	private Job transactionalJob;

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Tasklet tasklet;

	@Test
	public void differentTransactionsOnRepetitiveInvocations() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

		when(tasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
				.thenReturn(RepeatStatus.CONTINUABLE)
				.thenReturn(RepeatStatus.CONTINUABLE)
				.thenReturn(RepeatStatus.FINISHED);

		JobExecution exec = jobLauncher.run(transactionalJob, jobParameters);

		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		assertThat(stepExec.getCommitCount()).isEqualTo(3);
	}

	@Test
	public void noTransactionInTasklet() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

		when(tasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
				.thenAnswer(new Answer<RepeatStatus>() {
					@Override
					public RepeatStatus answer(InvocationOnMock invocation) throws Throwable {
						noActiveTransaction();
						return RepeatStatus.FINISHED;
					}
				});

		JobExecution exec = jobLauncher.run(noTransactionJob, jobParameters);
		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		assertThat(stepExec.getCommitCount()).isEqualTo(1);
	}

	protected void noActiveTransaction() {
		assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isFalse();
	}

	private void isTransactionActive() {
		assertThat(TransactionSynchronizationManager.isActualTransactionActive()).isTrue();
	}

	private class myTasklet implements Tasklet {
		@Override
		public RepeatStatus execute(StepContribution contribution,
		                            ChunkContext chunkContext) throws Exception {
			return RepeatStatus.FINISHED;
		}
	}
}
