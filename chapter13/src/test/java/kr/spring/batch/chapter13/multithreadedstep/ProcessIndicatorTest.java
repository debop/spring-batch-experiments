package kr.spring.batch.chapter13.multithreadedstep;

import kr.spring.batch.chapter13.domain.Product;
import kr.spring.batch.chapter13.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter13.multithreadedstep.ProcessIndicatorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:55
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:/kr/spring/batch/chapter13/multithreadedstep/process-indicator-context.xml" })
public class ProcessIndicatorTest {

    @Autowired
    private JobLauncher launcher;

    @Autowired
    @Qualifier("readWriteMultiThreadJob")
    private Job multiThreadedJob;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @PersistenceContext
    EntityManager em;

    @Before
    public void initializeDatabase() {
        int count = 55;
        for (int i = 0; i < count; i++) {
            Product p = new Product(String.valueOf(i));
            p.setName("Proudct " + i);
            p.setPrice(124.60f);
            productRepository.save(p);
        }
        productRepository.flush();
    }

    @Test
    public void multiThreadedStep() throws Exception {
        JobExecution multiThreadedJobExec = launcher.run(multiThreadedJob, new JobParameters());

        assertThat(multiThreadedJobExec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        long countNotProcessed = productRepository.countByNotProcessed();
        assertThat(countNotProcessed).isEqualTo(0);
    }

}
