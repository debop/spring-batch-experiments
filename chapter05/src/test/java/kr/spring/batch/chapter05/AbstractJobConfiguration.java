package kr.spring.batch.chapter05;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * kr.experiments.springbatch.AbstractJobConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오전 9:35
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import({ SpringLaunchConfiguration.class })
public class AbstractJobConfiguration {

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

    @Bean
    public DataSource dataSource() {
        AbstractJobConfiguration.log.info("create DataSource");

        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
            .addScript("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
            .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
     * 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
     * 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.
     *
     * @return
     * @throws Exception
     */
    @Bean
    public TaskExecutor taskExecutor() throws Exception {

        // NOTE: TaskExecutor 를 제공하는 것은 비동기 방식으로 작업을 수행한다는 뜻입니다.
        // NOTE: 테스트 시에는 작업 진행 중에 테스트가 종료될 수 있으므로 옳바른 테스트 결과가 안나올 수 있습니다.
        // HINT: 테스트 시에는 null 을 주시면 동기방식으로 처리되어 모든 작업이 끝나야 테스트 메소드가 종료됩니다.

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(32);
        executor.afterPropertiesSet();
        return executor;
//        return null;
    }
}
