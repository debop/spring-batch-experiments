package kr.spring.batch.chapter14.test.batch.integration.job;

import kr.spring.batch.chapter14.batch.ImportValidator;
import kr.spring.batch.chapter14.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.integration.job.WholeBatchTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 10:17
 */
@TestExecutionListeners(
		{
				DependencyInjectionTestExecutionListener.class,
				StepScopeTestExecutionListener.class
		})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-batch-job.xml" })
public class WholeBatchTest {

	String PRODUCTS_PATH = "classpath:kr/spring/batch/chapter14/input/products.txt";
	String STATISTIC_PATH = "file:./target/statistic.txt";

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DirtiesContext
	public void integration() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString(ImportValidator.PARAM_INPUT_RESOURCE, PRODUCTS_PATH)
				.addString(ImportValidator.PARAM_REPORT_RESOURCE, STATISTIC_PATH)
				.toJobParameters();

		JobExecution exec = jobLauncherTestUtils.launchJob(jobParameters);
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		StepExecution stepExec = exec.getStepExecutions().iterator().next();
		assertThat(stepExec.getFilterCount()).isEqualTo(2);     // 하나는 NULL, 하나는 음수
		assertThat(stepExec.getWriteCount()).isEqualTo(6);      // 총 8개 중에 2개 제외
		assertThat(productRepository.count()).isEqualTo(6);
	}
}
