package kr.spring.batch.chapter14.test.batch.integration.processor;

import kr.spring.batch.chapter14.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.integration.processor.CompositeItemProcessorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 10:07
 */
@TestExecutionListeners(
		{
				DependencyInjectionTestExecutionListener.class,
				StepScopeTestExecutionListener.class
		})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-batch-job.xml" })
public class CompositeItemProcessorTest {

	@Autowired
	private ItemProcessor<Product, Product> processor;

	public StepExecution getStepExecution() {
		JobParameters jobParameters = new JobParametersBuilder()
				.addDouble("maxPrice", 200.00d)
				.toJobParameters();

		StepExecution execution = MetaDataInstanceFactory.createStepExecution(jobParameters);
		return execution;
	}

	@Test
	@DirtiesContext
	public void testProcessor() throws Exception {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(100.0f));

		Product p2 = processor.process(p1);
		assertThat(p2).isNotNull();
	}

	@Test
	@DirtiesContext
	public void testZeroPriceFailure() throws Exception {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(0.0f));

		Product p2 = processor.process(p1);
		assertThat(p2).isNull();
	}

	@Test
	@DirtiesContext
	public void testNegativePriceFailure() throws Exception {
		Product p1 = new Product();
		p1.setPrice(new BigDecimal(-100.0f));

		Product p2 = processor.process(p1);
		assertThat(p2).isNull();
	}

	@Test
	@DirtiesContext
	public void testEmptyProductFailure() throws Exception {
		Product p1 = new Product();
		Product p2 = processor.process(p1);
		assertThat(p2).isNull();
	}
}
