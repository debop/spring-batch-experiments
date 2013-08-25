package kr.spring.batch.chapter13.parallel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter13.parallel.ParallelStepTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오전 11:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration({ "/kr/spring/batch/chapter13/parallel/parallel-step-context.xml" })
@ContextConfiguration(classes = { ParallelStepConfiguration.class })
public class ParallelStepTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("parallelImportProductsJob")
	private Job parallelImportProductsJob;

	@Test
	public void multithreadedStep() throws Exception {
		JobExecution parallelImportProductsJobExec =
				jobLauncher.run(parallelImportProductsJob, new JobParameters());
	}
}
