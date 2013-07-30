package kr.experiments.springbatch.chapter02.config;

import kr.experiments.springbatch.config.LaunchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * kr.experiments.springbatch.chapter02.config.RootDatabaseConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 11:13
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import(LaunchConfiguration.class)
public class RootDatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        log.info("create DataSource");

        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
                .addScript("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
                .addScript("classpath:/create-tables.sql")
                .build();
    }
}
