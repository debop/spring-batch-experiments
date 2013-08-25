package kr.spring.batch.chapter13.multithreadedstep;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter13.multithreadedstep.MultithreadedStepTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:54
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { MultithreadedStepConfiguration.class })
public class MultithreadedStepTest {

	@Autowired
	private JobLauncher launcher;

	@Autowired
	private Job multiThreadedJob;

	@Test
	public void testMultiThreadedStep() throws Exception {
		long count = 55;
		JobExecution multithreadJobExec = launcher.run(
				multiThreadedJob,
				new JobParametersBuilder()
						.addLong("count", count)
						.toJobParameters());
	}
}
