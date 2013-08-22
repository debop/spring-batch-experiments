package kr.spring.batch.chapter14.test.batch.integration.step;

import kr.spring.batch.chapter14.domain.Product;
import kr.spring.batch.chapter14.repository.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.integration.step.StatisticStepTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 9:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/infrastructure-job.xml")
public class StatisticStepTest {

    String STATISTIC_REF_PATH = "classpath:kr/spring/batch/chapter14/output/statistic-summary.txt";
    String STATISTIC_PATH = "./target/statistic-summary.txt";

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setup() {
        productRepository.deleteAll();
        productRepository.flush();

        Product product = new Product();
        product.setId("1");
        product.setDescription("");
        product.setPrice(new BigDecimal(10.0f));

        productRepository.saveAndFlush(product);

        product = new Product();
        product.setId("2");
        product.setDescription("");
        product.setPrice(new BigDecimal(30.0f));

        productRepository.saveAndFlush(product);
    }

    @Test
    @DirtiesContext
    public void integration() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("reportResource", "file:" + STATISTIC_PATH)
            .toJobParameters();

        JobExecution exec = jobLauncherTestUtils.launchStep("statisticStep", jobParameters);

        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        StepExecution stepExecution = exec.getStepExecutions().iterator().next();
        assertThat(stepExecution.getWriteCount()).isEqualTo(1);

        AssertFile.assertFileEquals(ResourceUtils.getFile(STATISTIC_REF_PATH),
                                    ResourceUtils.getFile(STATISTIC_PATH));
    }
}
