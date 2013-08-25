package kr.spring.batch.chapter09.test.transaction;

import kr.spring.batch.chapter09.test.AbstractBatchConfiguration;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

/**
 * kr.spring.batch.chapter09.test.transaction.TaskletTransactionConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오후 1:40
 */
@Configuration
@EnableBatchProcessing
public class TaskletTransactionConfiguration extends AbstractBatchConfiguration {

	@Bean
	public Tasklet tasklet() {
		return Mockito.mock(Tasklet.class);
	}

	@Bean
	public Job transactionalJob() {
		Step step = stepBuilders.get("transactionalStep").tasklet(tasklet()).build();
		return jobBuilders.get("transactionalJob").start(step).build();
	}

	@Bean
	public Job noTransactionJob() {
		Step step = stepBuilders.get("noTransactionalStep")
		                        .tasklet(tasklet())
		                        .transactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NEVER))
		                        .build();
		return jobBuilders.get("noTransactionalJob").start(step).build();
	}
}
