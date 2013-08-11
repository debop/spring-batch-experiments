package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractJobTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter07.test.process.FilteringExistingProductTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 5:43
 */
@Slf4j
@ContextConfiguration(classes = { FilteringExistingProductConfiguration.class })
public class FilteringExistingProductTest extends AbstractJobTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	@Autowired
	private ProductRepository productRepository;

	@Before
	public void setUp() {
		productRepository.deleteAll();

		Product p = new Product();
		p.setId("214");
		productRepository.save(p);
		p = new Product();
		p.setId("215");
		productRepository.save(p);
		productRepository.flush();
	}

	@Test
	public void changingState() throws Exception {
		JobExecution exec = jobLauncher.run(job,
		                                    new JobParametersBuilder()
				                                    .addString("inputFile", "/products.txt")
				                                    .toJobParameters());
		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		StepExecution stepExecution = exec.getStepExecutions().iterator().next();
		assertThat(stepExecution.getFilterCount()).isEqualTo(2);
		assertThat(stepExecution.getWriteCount()).isEqualTo(6);
	}
}
