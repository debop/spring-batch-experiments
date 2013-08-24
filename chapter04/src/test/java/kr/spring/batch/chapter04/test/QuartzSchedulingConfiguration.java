package kr.spring.batch.chapter04.test;

import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.scheduling.quartz.JobDetailBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * kr.spring.batch.chapter04.test.QuartzSchedulingConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 24. 오후 10:23
 */
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public class QuartzSchedulingConfiguration {

	@Autowired public JobBuilderFactory jobBuilders;
	@Autowired public StepBuilderFactory stepBuilders;
	@Autowired JobLauncher jobLauncher;

	@Bean
	public org.springframework.batch.core.Job job() {
		Step step = stepBuilders.get("step").tasklet(countDownTasklet()).build();
		return jobBuilders.get("job").start(step).build();
	}

	@Bean
	public CountDownTasklet countDownTasklet() {
		return new CountDownTasklet(countDownLatch());
	}

	@Bean
	public CountDownLatch countDownLatch() {
		return new CountDownLatch(5);
	}

	@Bean
	public JobDetail jobDetail() {
		JobDetailBean jobDetail = new JobDetailBean();
		jobDetail.setBeanName("jobDetail");
		jobDetail.setJobClass(QuartzLauncher.class);
		jobDetail.afterPropertiesSet();
		return jobDetail;
	}

	@Bean
	public CronTrigger jobDetailTrigger() throws Exception {
		CronTriggerBean cronTriggerBean = new CronTriggerBean();
		cronTriggerBean.setBeanName("jobDetailTrigger");
		cronTriggerBean.setJobDetail(jobDetail());
		cronTriggerBean.setCronExpression("* * * * * ?");
		cronTriggerBean.afterPropertiesSet();
		return cronTriggerBean;
	}

	@Bean
	public Scheduler scheduler() throws Exception {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setTriggers(new Trigger[] { jobDetailTrigger() });
		factory.setJobFactory(new SpringBeanJobFactory());
		factory.setDataSource(dataSource());
		factory.setSchedulerContextAsMap(new HashMap<String, Object>() {{
			put("job", job());
			put("jobLauncher", jobLauncher);
		}});
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:kr/spring/batch/chapter04/test/tables_h2.sql")
				.build();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
