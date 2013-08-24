package kr.spring.batch.chapter04.test.stop;

import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

/**
 * kr.spring.batch.chapter04.test.stop.StopConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 24. 오후 7:26
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class })
public class StopConfiguration {

	@Autowired JobBuilderFactory jobBuilders;
	@Autowired StepBuilderFactory stepBuilders;


	@Bean
	public Job readWriteJob() {
		Step step = stepBuilders.get("readWriteStep")
		                        .<String, String>chunk(10)
		                        .reader(reader())
		                        .writer(writer())
		                        .listener((ItemReadListener) stopListener())
		                        .build();
		return jobBuilders.get("readWriterJob").start(step).build();
	}

	@Bean
	public InfiniteReader reader() {
		return new InfiniteReader();
	}

	@Bean
	public EmptyWriter writer() {
		return new EmptyWriter();
	}

	@Bean
	public StopListener stopListener() {
		return new StopListener();
	}

	@Bean
	public Job taskletJob() {
		Step step = stepBuilders.get("taskletJob").tasklet(tasklet()).build();
		return jobBuilders.get("taskletJob").start(step).build();
	}

	@Bean
	public ProcessItemsTasklet tasklet() {
		return new ProcessItemsTasklet();
	}

	@Bean
	public Job jobOperatorJob() {
		Step step = stepBuilders.get("jobOperatorStep")
		                        .<String, String>chunk(10)
		                        .reader(reader())
		                        .writer(writer())
		                        .build();
		return jobBuilders.get("jobOperatorStep").start(step).build();
	}

	@Bean
	public MBeanExporter mBeanExporter(JobOperator jobOperator) {
		Map<String, Object> map = new HashMap<>();
		map.put("kr.spring.batch:name=jobOperator", jobOperator);

		MBeanExporter exporter = new MBeanExporter();
		exporter.setBeans(map);

		return exporter;
	}

	@Bean(name = "jobDataSource")
	public EmbeddedDatabase jobDataSource() {
		log.info("create DataSource");

		return new EmbeddedDatabaseBuilder()
				.setName("JobRepository")
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
				.addScript("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
				.build();
	}

	@Bean(name = "jobTransactionManager")
	public PlatformTransactionManager jobTransactionManager() {
		return new DataSourceTransactionManager(jobDataSource());
	}

	/**
	 * TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
	 * 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
	 * 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.
	 *
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "jobTaskExecutor")
	public TaskExecutor jobTaskExecutor() throws Exception {

		// NOTE: TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
		// NOTE: 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
		// HINT: 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.

		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

		// HINT: 이 테스트에서는 Pool을 하나로 해야 중지를 시킬 수 있다.
		executor.setMaxPoolSize(1);
		executor.afterPropertiesSet();
		return executor;
	}
}
