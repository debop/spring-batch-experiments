package kr.spring.batch.chapter07.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * kr.experiments.springbatch.SpringLaunchConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오전 9:18
 */
@Slf4j
@Configuration
@EnableBatchProcessing
public class SpringLaunchConfiguration {

    @Bean
    public PropertyPlaceholderConfigurer placeHolderProperties() throws Exception {

        SpringLaunchConfiguration.log.info("create PropertyPlaceholderConfigurer");
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
//        configurer.setLocation(new ClassPathResource("batch.properties"));
//        configurer.setSystemPropertiesModeName("SYSTEM_PROPERTIES_MODE_OVERRIDE");
//        configurer.setIgnoreUnresolvablePlaceholders(true);
//        configurer.setOrder(1);

        return configurer;
    }

    @Bean
    public JobOperator jobOperator(JobLauncher jobLauncher,
                                   JobRepository jobRepository,
                                   JobExplorer jobExplorer,
                                   JobRegistry jobRegistry) throws Exception {
        SimpleJobOperator jobOperator = new SimpleJobOperator();
        jobOperator.setJobLauncher(jobLauncher);
        jobOperator.setJobRepository(jobRepository);
        jobOperator.setJobExplorer(jobExplorer);
        jobOperator.setJobRegistry(jobRegistry);

        jobOperator.afterPropertiesSet();
        return jobOperator;
    }

    @Bean
    public JobExplorer jobExplorer(@Qualifier("jobDataSource") DataSource dataSource) throws Exception {
        JobExplorerFactoryBean factory = new JobExplorerFactoryBean();
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();

        return (JobExplorer) factory.getObject();
    }

    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry());
        return postProcessor;
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository,
                                   @Qualifier("jobTaskExecutor") TaskExecutor taskExecutor) throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        if (taskExecutor != null)
            jobLauncher.setTaskExecutor(taskExecutor);

        return jobLauncher;
    }

    @Bean
    public JobRepository jobRepository(@Qualifier("jobDataSource") DataSource dataSource,
                                       @Qualifier("jobTransactionManager") PlatformTransactionManager transactionManager) throws Exception {
        SpringLaunchConfiguration.log.info("create JobRepository...");

        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setDatabaseType(DatabaseType.HSQL.name());
        factory.setTransactionManager(transactionManager);

        // HINT: 에러메시지 Standard JPA does not support custom isolation levels - use a special JpaDialect for your JPA implementation 가 나올 때
        // HINT: http://forum.springsource.org/showthread.php?59779-Spring-Batch-1-1-2-Standard-JPA-does-not-support-custom-isolation-levels-use-a-sp
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");

        factory.afterPropertiesSet();

        return factory.getJobRepository();
    }

    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository) throws Exception {
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository,
                                                 @Qualifier("jobTransactionManager")
                                                 PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilderFactory(jobRepository, transactionManager);
    }

    // NOTE: JobLauncherTestUtils 는 테스트 대상 Job 이 한개만 있어야 한다.
//    @Bean
//    public JobLauncherTestUtils jobLauncherTestUtils() {
//        return new JobLauncherTestUtils();
//    }

}
