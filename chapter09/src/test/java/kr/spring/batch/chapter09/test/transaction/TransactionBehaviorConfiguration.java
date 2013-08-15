package kr.spring.batch.chapter09.test.transaction;

import kr.experiments.springbatch.chapter01.domain.Product;
import kr.spring.batch.chapter09.test.AbstractJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.batch.item.validator.ValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DeadlockLoserDataAccessException;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jmx.access.MBeanProxyFactoryBean;

import javax.management.MalformedObjectNameException;

/**
 * kr.spring.batch.chapter09.test.transaction.TransactionBehaviorConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오후 3:20
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class TransactionBehaviorConfiguration extends AbstractJobConfiguration {

	@Bean
	public Job job() {
		Step step = stepBuilders.get("step")
		                        .<String, String>chunk(5)
		                        .faultTolerant().skipLimit(5).skip(ValidationException.class)
		                        .reader(reader())
		                        .processor(processor())
		                        .writer(writer())
		                        .build();

		return jobBuilders.get("job").start(step).build();
	}

	@Bean
	public Job noRollbackJob() {
		Step step = stepBuilders.get("noRollbackStep")
		                        .<String, String>chunk(5)
		                        .faultTolerant()
		                        .skipLimit(5)
		                        .skip(ValidationException.class)
		                        .noRollback(ValidationException.class)
		                        .reader(reader())
		                        .processor(processor())
		                        .writer(writer())
		                        .build();

		return jobBuilders.get("noRollbackJob").start(step).build();
	}

	@Bean
	public Job notTransactionalReaderJob() {
		Step step = stepBuilders.get("notTransactionalReaderStep")
		                        .<String, String>chunk(5)
		                        .faultTolerant()
		                        .skipLimit(5)
		                        .skip(ValidationException.class)
		                        .reader(reader())
		                        .processor(processor())
		                        .writer(writer())
		                        .build();

		return jobBuilders.get("notTransactionalReaderJob").start(step).build();
	}

	@Bean
	public Job transactionalReaderJob() {
		Step step = stepBuilders.get("transactionalReaderStep")
		                        .<String, String>chunk(5)
				.readerIsTransactionalQueue()                   // Tx를 위해 reader 정보를 Queue에 넣습니다.
				.faultTolerant()
				.skipLimit(5)
				.skip(DeadlockLoserDataAccessException.class)
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.build();

		return jobBuilders.get("transactionalReaderJob").start(step).build();
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory ccf = new CachingConnectionFactory();
		ccf.setTargetConnectionFactory(new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false"));
		return ccf;
	}

	@Bean
	public ActiveMQQueue productQueue() {
		return new ActiveMQQueue("spring.batch.queue.product");
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory());
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.setDefaultDestination(productQueue());
		jmsTemplate.setReceiveTimeout(100);
		return jmsTemplate;
	}

	@Bean
	public JmsItemReader<Product> jmsReader() {
		JmsItemReader<Product> reader = new JmsItemReader<Product>();
		reader.setJmsTemplate(jmsTemplate());
		return reader;
	}

	@Bean
	public MBeanProxyFactoryBean productQueueView() {
		MBeanProxyFactoryBean bean = new MBeanProxyFactoryBean();
		bean.setProxyInterface(org.apache.activemq.broker.jmx.QueueViewMBean.class);
		try {
			bean.setObjectName("org.apache.activemq:BrokerName=embedded,Type=Queue,Destination=spring.batch.queue.product");
		} catch (MalformedObjectNameException e) {
			log.error("ActiveMQ의 ObjectName이 잘못되었습니다.", e);
		}
		return bean;
	}

}
