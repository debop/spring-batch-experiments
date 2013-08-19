package kr.spring.batch.chapter14.test.batch.integration.step;

import kr.spring.batch.chapter14.batch.ImportValidator;
import kr.spring.batch.chapter14.repository.ProductRepository;
import kr.spring.batch.chapter14.test.batch.integration.BatchConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.util.ResourceUtils;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.integration.step.ProductStepTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 11:43
 */
@TestExecutionListeners(
    {
        DependencyInjectionTestExecutionListener.class,
        StepScopeTestExecutionListener.class
    })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { BatchConfiguration.class })
public class ProductStepTest {

    String PRODUCTS_PATH = "classpath:kr/spring/batch/chapter14/input/products.txt";
    String EXCLUDE_REF_PATH = "classpath:kr/spring/batch/chapter14/output/excludes.txt";
    String EXCLUDES_PATH = "file:./target/excludes.txt";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DirtiesContext
    public void testIntegration() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString(ImportValidator.PARAM_INPUT_RESOURCE, PRODUCTS_PATH)
            .toJobParameters();

        JobExecution exec = jobLauncherTestUtils.launchStep("productsStep", jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        StepExecution stepExecution = exec.getStepExecutions().iterator().next();
        assertThat(stepExecution.getFilterCount()).isEqualTo(2);
        assertThat(stepExecution.getWriteCount()).isEqualTo(6);  // 전체 8 개에서 2개를 제외합니다.
        assertThat(productRepository.count()).isEqualTo(6);      // DB에 저장된 갯수

        AssertFile.assertFileEquals(ResourceUtils.getFile(EXCLUDE_REF_PATH),
                                    ResourceUtils.getFile(EXCLUDES_PATH));
    }

}
