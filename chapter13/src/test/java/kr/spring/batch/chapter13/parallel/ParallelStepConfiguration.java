package kr.spring.batch.chapter13.parallel;

import kr.spring.batch.chapter13.AbstractBatchConfiguration;
import kr.spring.batch.chapter13.JpaHSqlConfiguration;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

/**
 * kr.spring.batch.chapter13.parallel.ParallelStepConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 25. 오후 7:38
 */
@Configuration
@EnableBatchProcessing
@Import({ JpaHSqlConfiguration.class })
@ImportResource({ "classpath:kr/spring/batch/chapter13/parallel/parallel-step-context.xml" })
public class ParallelStepConfiguration extends AbstractBatchConfiguration {
}
