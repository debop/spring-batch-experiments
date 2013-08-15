package kr.spring.batch.chapter09.test.jta;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;
import kr.spring.batch.chapter09.repository.OrderRepository;
import kr.spring.batch.chapter09.test.JpaHSqlConfiguration;
import kr.spring.batch.chapter09.test.SpringLaunchConfiguration;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.jta.JtaTransactionManager;

import java.util.Properties;

/**
 * kr.spring.batch.chapter09.test.jta.JtaConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 11:08
 */
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { OrderRepository.class })
@Import({ SpringLaunchConfiguration.class, JpaHSqlConfiguration.class })
@ImportResource({ "classpath:job-context.xml" })
public class JtaConfiguration {

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

	@Bean(initMethod = "init", destroyMethod = "close")
	public PoolingDataSource batchMetaDataDataSource() {
		PoolingDataSource ds = new PoolingDataSource();

		ds.setClassName(org.hsqldb.jdbc.pool.JDBCXADataSource.class.getName());
		ds.setUniqueName("batchdb");
		ds.setMaxPoolSize(100);

		Properties props = new Properties();
		props.setProperty("databaseName", "spring-batch-metadata");
		// props.setProperty("createDatabase", "create");
		ds.setDriverProperties(props);

		ds.setAllowLocalTransactions(true);

		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(batchDropSchemaScript);
		populator.addScript(batchCreateSchemaScript);
		DatabasePopulatorUtils.execute(populator, ds);

		return ds;
	}

	@Value("classpath:/kr/spring/batch/chapter09/jta/drop-tables.sql")
	private Resource dropTablesScript;
	@Value("classpath:/kr/spring/batch/chapter09/jta/create-tables.sql")
	private Resource createTableScript;

	@Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
	public PoolingDataSource applicationDataSource() {
		PoolingDataSource ds = new PoolingDataSource();

		ds.setClassName(org.hsqldb.jdbc.pool.JDBCXADataSource.class.getName());
		ds.setUniqueName("appdb");
		ds.setMaxPoolSize(100);

		final Properties props = new Properties();
		props.setProperty("databaseName", "chapter09-application");
		// props.setProperty("createDatabase", "create");
		ds.setDriverProperties(props);

		ds.setAllowLocalTransactions(true);

		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(dropTablesScript);
		populator.addScript(createTableScript);
		DatabasePopulatorUtils.execute(populator, ds);

		return ds;
	}

	@Bean
	public bitronix.tm.Configuration btmConfig() {
		bitronix.tm.Configuration cfg = TransactionManagerServices.getConfiguration();
		cfg.setServerId("spring-btm");
		return cfg;
	}

	@Bean(destroyMethod = "shutdown")
	@DependsOn({ "btmConfig" })
	public BitronixTransactionManager bitronixTransactionManager() {
		return TransactionManagerServices.getTransactionManager();
	}

	@Bean
	public JtaTransactionManager transactionManager() {
		JtaTransactionManager tm = new JtaTransactionManager();
		tm.setTransactionManager(bitronixTransactionManager());
		tm.setUserTransaction(bitronixTransactionManager());
		tm.setAllowCustomIsolationLevels(true);

		return tm;
	}
}
