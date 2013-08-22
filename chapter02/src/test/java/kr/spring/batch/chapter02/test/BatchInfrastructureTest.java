package kr.spring.batch.chapter02.test;

import kr.spring.batch.chapter02.config.RootDatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter02.test.BatchInfrastructureTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 11:12
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootDatabaseConfiguration.class })
public class BatchInfrastructureTest {

    @Autowired
    JobRepository jobRepository;

    @Test
    public void jobInstanceExists() {
        Assertions.assertThat(jobRepository.isJobInstanceExists("dummy-job", new JobParameters())).isFalse();
    }
}
