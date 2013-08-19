package kr.spring.batch.chapter14.test.batch.integration;

import kr.spring.batch.chapter14.config.AbstractJobConfiguration;
import kr.spring.batch.chapter14.config.JpaHSqlConfiguration;
import kr.spring.batch.chapter14.repository.ProductRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * kr.spring.batch.chapter14.test.batch.integration.BatchConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 11:43
 */
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
@ImportResource({ "/kr/spring/batch/chapter14/spring/test-job-context.xml" })
public class BatchConfiguration extends AbstractJobConfiguration {

    // NOTE: JobLauncherTestUtils 는 테스트 대상 Job 이 한개만 있어야 한다.
    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }
}
