package kr.spring.batch.chapter09.test;

import kr.spring.batch.chapter09.test.beans.BusinessService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter08.test.AbstractRobustnessTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractRobustnessTest {

	@Autowired
	protected JobLauncher jobLauncher;

//	@Autowired
//	protected Job job;

	@Autowired
	protected BusinessService service;

	@Autowired
	protected SkipListener<?, ?> skipListener;

	@Before
	public void setUp() {
		Mockito.reset(service);
		Mockito.reset(skipListener);
	}


	protected void configureServiceForRead(BusinessService service, int count) {
		List<String> args = new ArrayList<String>();
		for (int i = 2; i <= count; i++)
			args.add(String.valueOf(i));
		args.add(null);
		Mockito.when(service.reading()).thenReturn("1", args.toArray(new String[0]));
	}

	protected void assertRead(int expectedRead, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getReadCount()).isEqualTo(expectedRead);
	}

	protected void assertWrite(int expectedWrite, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getWriteCount()).isEqualTo(expectedWrite);
	}

	protected void assertProcessSkip(int expectedProcessSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getProcessSkipCount()).isEqualTo(expectedProcessSkip);
	}

	protected void assertReadSkip(int expectedReadSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getReadSkipCount()).isEqualTo(expectedReadSkip);
	}

	protected void assertWriteSkip(int expectedWriteSkip, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getWriteSkipCount()).isEqualTo(expectedWriteSkip);
	}

	protected void assertCommit(int expectedCommit, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getCommitCount()).isEqualTo(expectedCommit);
	}

	protected void assertRollback(int expectedRollback, JobExecution exec) {
		StepExecution stepExec = getStepExecution(exec);
		assertThat(stepExec.getRollbackCount()).isEqualTo(expectedRollback);
	}

	protected StepExecution getStepExecution(JobExecution exec) {
		return exec.getStepExecutions().iterator().next();
	}
}
