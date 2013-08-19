package kr.spring.batch.chapter14.test.batch.integration.reader;

import kr.spring.batch.chapter14.batch.ImportValidator;
import kr.spring.batch.chapter14.domain.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.integration.reader.ReaderWithListenerTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 11:07
 */
@TestExecutionListeners(
    {
        DependencyInjectionTestExecutionListener.class,
        StepScopeTestExecutionListener.class
    })
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes = { BatchConfiguration.class })
@ContextConfiguration({ "classpath:spring-batch-job.xml" })
public class ReaderWithListenerTest {

    String PRODUCTS_PATH = "classpath:kr/spring/batch/chapter14/input/products.txt";

    @Autowired
    private ItemReader<Product> reader;

    public StepExecution getStepExecution() {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString(ImportValidator.PARAM_INPUT_RESOURCE, PRODUCTS_PATH)
            .toJobParameters();

        StepExecution execution = MetaDataInstanceFactory.createStepExecution(jobParameters);
        return execution;
    }

    @Before
    public void setUp() {
        ((ItemStream) reader).open(new ExecutionContext());
    }

    @After
    public void tearDown() {
        ((ItemStream) reader).close();
    }

    @Test
    @DirtiesContext
    public void testReader() throws Exception {
        Product p = reader.read();
        assertThat(p).isNotNull();
        assertThat(p.getId()).isEqualTo("211");
        assertThat(reader.read()).isNotNull();   // 2
        assertThat(reader.read()).isNotNull();  // 3
        assertThat(reader.read()).isNotNull();  // 4
        assertThat(reader.read()).isNotNull();  // 5
        assertThat(reader.read()).isNotNull();  // 6
        assertThat(reader.read()).isNotNull();  // 7
        assertThat(reader.read()).isNotNull();  // 8

        assertThat(reader.read()).isNull();
    }
}
