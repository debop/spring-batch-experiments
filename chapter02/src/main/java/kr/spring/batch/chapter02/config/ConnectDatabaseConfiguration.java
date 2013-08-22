package kr.spring.batch.chapter02.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter02.config.ConnectDatabaseConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 11:14
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import(LaunchConfiguration.class)
public class ConnectDatabaseConfiguration {

    @Bean
    public DataSource dataSource() {
        log.info("create DataSource");

        return new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
//                .addScript("classpath:/org/springframework/batch/core/schema-drop-h2.sql")
//                .addScript("classpath:/org/springframework/batch/core/schema-h2.sql")
//                .addScript("classpath:/create-tables.sql")
            .build();
    }
}
