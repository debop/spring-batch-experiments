package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractBatchTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter07.test.process.ItemProcessorConfigurationTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:25
 */
@Slf4j
@ContextConfiguration(classes = { CompositeProcessingConfiguration.class })
public class CompositeProcessingTest extends AbstractBatchTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job jobWithItemProcessor;

	@Autowired
	private ProductRepository productRepository;

	@Before
	public void setUp() {
		super.setUp();
		productRepository.deleteAll();
	}

	@Test
	public void changingState() throws Exception {
		JobExecution exec = jobLauncher.run(jobWithItemProcessor,
		                                    new JobParametersBuilder()
				                                    .addString("inputFile", "/partner-products.txt")
				                                    .toJobParameters());

		assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
		assertThat(productRepository.count()).isEqualTo(8);
		for (Product product : productRepository.findAll()) {
			assertThat(product.getId()).startsWith("PR");
		}
	}
}
