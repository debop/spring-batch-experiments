package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractJobTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter07.test.process.ItemProcessorConfigurationTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:25
 */
@Slf4j
@ContextConfiguration(classes = { ChangingStateProcessingConfiguration.class })
public class ChangingStateProcessingTest extends AbstractJobTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("readWriteJob")
    private Job jobWithItemProcessor;

    @Autowired
    @Qualifier("readWriteJobPojo")
    private Job jobWithAdapter;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        super.setUp();
        productRepository.deleteAll();
    }

    @Test
    public void changingState() throws Exception {
        JobExecution exec = jobLauncher.run(jobWithItemProcessor,
                                            new JobParametersBuilder()
                                                .addString("inputFile", "/products.txt")
                                                .toJobParameters());

        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(productRepository.count()).isEqualTo(8);
        for (Product product : productRepository.findAll()) {
            assertThat(product.getId()).startsWith("PR");
            assertThat(product.getName()).endsWith("(P1)");
        }
    }

    @Test
    public void changingStateWithAdapter() throws Exception {
        JobExecution exec = jobLauncher.run(jobWithAdapter,
                                            new JobParametersBuilder()
                                                .addString("inputFile", "/products.txt")
                                                .toJobParameters());

        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(productRepository.count()).isEqualTo(8);
        for (Product product : productRepository.findAll()) {
            assertThat(product.getId()).startsWith("PR");
            assertThat(product.getName()).endsWith("(P1)");
        }
    }
}
