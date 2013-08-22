package kr.spring.batch.chapter14.test.batch.integration.reader;

import kr.spring.batch.chapter14.batch.ImportValidator;
import kr.spring.batch.chapter14.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Callable;

/**
 * kr.spring.batch.chapter14.test.batch.integration.reader.ReaderWithStepScopeTestUtilsTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 8:47
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring/infrastructure-job.xml" })
public class ReaderWithStepScopeTestUtilsTest {

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

    @Test
    @DirtiesContext
    public void testReader() throws Exception {
        int count = StepScopeTestUtils.doInStepScope(
            getStepExecution(),
            new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    int count = 0;
                    try {
                        ((ItemStream) reader).open(new ExecutionContext());
                        Object line = null;
                        while ((line = reader.read()) != null) {
                            log.info("Read Item=[{}]", line);
                            count++;
                        }
                        return count;
                    } finally {
                        ((ItemStream) reader).close();
                    }
                }
            });

        Assertions.assertThat(count).isEqualTo(8);

    }
}
