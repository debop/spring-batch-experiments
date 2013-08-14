package kr.spring.batch.chapter08.test.restart;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.restart.FilesInDirectoryItemReader;
import kr.spring.batch.chapter08.test.JpaHSqlConfiguration;
import kr.spring.batch.chapter08.test.SpringLaunchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.File;

/**
 * kr.spring.batch.chapter08.test.restart.RestartConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 14. 오전 11:02
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ SpringLaunchConfiguration.class, JpaHSqlConfiguration.class })
public class RestartConfiguration {

    @Autowired
    protected JobBuilderFactory jobBuilders;

    @Autowired
    protected StepBuilderFactory stepBuilders;

    @Bean
    public Job restartJob() {
        Step step = stepBuilders.get("restartStep")
                                .<File, File>chunk(3)
                                .reader(reader())
                                .writer(writer())
                                .build();

        return jobBuilders.get("restartJob")
                          .start(step)
                          .build();
    }

    @Bean
    public FilesInDirectoryItemReader reader() {
        FilesInDirectoryItemReader reader = new FilesInDirectoryItemReader();
        reader.setDirectory("./src/test/resources/restart/inputs");
        return reader;
    }

    @Bean
    @SuppressWarnings("unchecked")
    public ItemWriter<File> writer() {
        return (ItemWriter<File>) Mockito.mock(ItemWriter.class);
    }

    @Bean(name = "jobDataSource")
    public DataSource jobDataSource() {
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

    @Bean(name = "jobTaskExecutor")
    public TaskExecutor jobTaskExecutor() throws Exception {
        return null;
    }
}
