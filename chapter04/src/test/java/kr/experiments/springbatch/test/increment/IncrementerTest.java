package kr.experiments.springbatch.test.increment;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.experiments.springbatch.test.increment.IncrementerTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 4:12
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { IncrementerConfiguration.class })
public class IncrementerTest {

	@Autowired Job job;
	@Autowired JobLauncher jobLauncher;
	@Autowired JobOperator jobOperator;
	@Autowired JobExplorer jobExplorer;
	@Autowired Tasklet tasklet;

	@Test
	@DirtiesContext
	public void jobLauncherThenIncrementer() throws Exception {

		Mockito.doReturn(RepeatStatus.FINISHED)
		       .doReturn(RepeatStatus.FINISHED)
		       .doThrow(new RuntimeException())
		       .doReturn(RepeatStatus.FINISHED)
		       .when(tasklet).execute(Mockito.any(StepContribution.class), Mockito.any(ChunkContext.class));

		JobParameters jobParams = new JobParametersBuilder()
				.addString("input.file", "products.zip")
				.toJobParameters();

		JobExecution exec1 = jobLauncher.run(job, jobParams);
		assertThat(exec1.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
		assertThat(getJobInstanceCount()).isEqualTo(1);

		Long idExec2 = jobOperator.startNextInstance(job.getName());
		assertThat(getJobExecution(idExec2).getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		assertThat(getJobInstanceCount()).isEqualTo(2);

		Long idExec3 = jobOperator.startNextInstance(job.getName());
		assertThat(getJobExecution(idExec3).getExitStatus()).isEqualTo(ExitStatus.FAILED);
		assertThat(getJobInstanceCount()).isEqualTo(3);

		Long idExec4 = jobOperator.startNextInstance(job.getName());
		assertThat(getJobExecution(idExec4).getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		assertThat(getJobInstanceCount()).isEqualTo(4);


	}

	private JobExecution getJobExecution(Long executionId) {
		return jobExplorer.getJobExecution(executionId);
	}

	private int getJobInstanceCount() {
		return jobExplorer.getJobInstances(job.getName(), 0, Integer.MAX_VALUE).size();
	}
}
