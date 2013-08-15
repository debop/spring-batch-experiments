package kr.spring.batch.chapter09.test;

import kr.spring.batch.chapter09.test.beans.BusinessService;
import kr.spring.batch.chapter09.test.beans.DummyItemProcessor;
import kr.spring.batch.chapter09.test.beans.DummyItemReader;
import kr.spring.batch.chapter09.test.beans.DummyItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter09.test.AbstractBatchConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 11:33
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import({ SpringLaunchConfiguration.class })
public abstract class AbstractJobConfiguration {

	@Autowired
	protected JobBuilderFactory jobBuilders;

	@Autowired
	protected StepBuilderFactory stepBuilders;

	@Autowired
	protected JobExplorer jobExplorer;

	@Autowired
	protected JobOperator jobOperator;

	@Autowired
	protected JobRegistry jobRegistry;


	@Value("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
	private Resource batchDropSchemaScript;
	@Value("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
	private Resource batchCreateSchemaScript;

	@Bean(name = "jobDataSource")
	public DataSource jobDataSource() {
		log.info("create DataSource");

//		return new EmbeddedDatabaseBuilder()
//				.setName("JobRepository")
//				.setType(EmbeddedDatabaseType.HSQL)
//				.addScript("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
//				.addScript("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
//				.build();

		SingleConnectionDataSource ds = new SingleConnectionDataSource();
		ds.setDriverClassName("org.hsqldb.jdbcDriver");
		ds.setUrl("jdbc:hsqldb:mem:test;MVCC=1;");
		ds.setUsername("sa");
		ds.setPassword("");
		ds.setSuppressClose(true);

		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		// populator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-drop-hsqldb.sql"));
		populator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-hsqldb.sql"));
		DatabasePopulatorUtils.execute(populator, ds);

		return ds;
	}

	@Bean(name = "jobTransactionManager")
	public DataSourceTransactionManager jobTransactionManager() {
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
		return null;
	}


	@Bean
	public SkipListener skipListener() {
		return Mockito.mock(SkipListener.class);
	}

	@Bean
	public BusinessService service() {
		return Mockito.mock(BusinessService.class);
	}

	@Bean
	public ItemReader<String> reader() {
		return new DummyItemReader(service());
	}

	@Bean
	public ItemProcessor<String, String> processor() {
		return new DummyItemProcessor(service());
	}

	@Bean
	public ItemWriter<String> writer() {
		return new DummyItemWriter(service());
	}
}
