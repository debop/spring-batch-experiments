package kr.spring.batch.chapter08.test.skip;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.jpa.repositories.SkippedProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter08.test.skip.SkipTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 3:09
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SkipConfiguration.class })
public class SkipTest {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    private Job importProductsJob;

    @Autowired
    private Job importProductsJobWithSkipPolicy;

    @Test
    public void jobWithNoSkip() throws Exception {
        int initialCount = countProducts();
        int initialSkippedCount = countSkippedProducts();

        JobParameters params = new JobParametersBuilder()
                .addString("inputFile", "classpath:skip/products_no_error.txt")
                .toJobParameters();

        JobExecution exec = jobLauncher.run(importProductsJob, params);

        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(countProducts()).isEqualTo(initialCount + 8);
        assertThat(getStepExec(exec).getSkipCount()).isEqualTo(0);
        assertThat(getStepExec(exec).getRollbackCount()).isEqualTo(0);
        assertThat(countSkippedProducts()).isEqualTo(initialSkippedCount + getStepExec(exec).getSkipCount());
    }


    @Autowired
    ProductRepository productRepository;

    @Autowired
    SkippedProductRepository skippedProductRepository;

    private int countProducts() {
        return Long.valueOf(productRepository.count()).intValue();
    }

    private int countSkippedProducts() {
        return Long.valueOf(skippedProductRepository.count()).intValue();
    }

    private StepExecution getStepExec(JobExecution exec) {
        return exec.getStepExecutions().iterator().next();
    }
}
