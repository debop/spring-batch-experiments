package kr.spring.batch.chapter10.test;

import kr.spring.batch.config.AbstractJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * kr.spring.batch.chapter10.test.JobWithDecidersConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 17. 오후 1:10
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@ImportResource({ "classpath:kr/spring/batch/chapter10/job-with-deciders.xml" })
public class JobWithDecidersConfiguration extends AbstractJobConfiguration {

}
