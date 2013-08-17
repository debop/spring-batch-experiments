package kr.spring.batch.chapter10.test;

import org.junit.runner.RunWith;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Set;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter10.test.ExternalizedJobTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 17. 오전 10:56
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:kr/spring/batch/chapter10/batch-infrastructure.xml",
		                      "classpath:kr/spring/batch/chapter10/externalized-job.xml" })
public class ExternalizedJobTest extends AbstractJobTest {

	@Autowired
	private JobOperator jobOperator;

	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems() {
		int stepsInSubJob = 3;
		int stepJob = 1;
		return super.getExpectedNbStepExecutionsDownloadedFileOkNoSkippedItems() - stepsInSubJob + stepJob;
	}

	@Override
	protected int getExpectedNbStepExecutionsDownloadedFileOkSkippedItems() {
		int stepsInSubJob = 3;
		int stepJob = 1;
		return super.getExpectedNbStepExecutionsDownloadedFileOkSkippedItems() - stepsInSubJob + stepJob;
	}

	@Override
	protected int getExpectedNbOfInvocationFileExists() {
		return 2;
	}

	@Override
	protected void extraAssertionsDowlnloadedFileOkNoSkippedItems() throws Exception {
		assertInstancesForSubJob();
	}

	@Override
	protected void extraAssertionsDowlnloadedFileOkSkippedItems() throws Exception {
		assertInstancesForSubJob();
	}

	@Override
	protected void extraAssertionsNoDownloadedFile() throws Exception {
		assertInstancesForSubJob();
	}

	public void assertInstancesForSubJob() throws Exception {
		Set<String> names = jobOperator.getJobNames();
		assertThat(names.size()).isEqualTo(2);

		for (String name : names) {
			List<Long> instances = jobOperator.getJobInstances(name, 0, Integer.MAX_VALUE);
			assertThat(instances.size()).isGreaterThan(0).isLessThanOrEqualTo(3);
		}
	}
}
