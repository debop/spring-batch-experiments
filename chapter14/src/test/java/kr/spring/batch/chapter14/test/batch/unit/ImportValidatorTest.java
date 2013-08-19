package kr.spring.batch.chapter14.test.batch.unit;

import kr.spring.batch.chapter14.batch.ImportValidator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.core.io.ResourceLoader;

import static kr.spring.batch.chapter14.batch.ImportValidator.PARAM_INPUT_RESOURCE;
import static kr.spring.batch.chapter14.batch.ImportValidator.PARAM_REPORT_RESOURCE;
import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter14.test.batch.unit.ImportValidatorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:39
 */
public class ImportValidatorTest {

    String PRODUCT_PATH = "classpath:kr/spring/batch/chapter14/input/products.txt";
    String STATISTIC_PATH = "file:./target/statistics.txt";

    private ResourceLoader resourceLoader;
    private ImportValidator validator;

    @Before
    public void setUp() {
        resourceLoader = mock(ResourceLoader.class, Mockito.RETURNS_DEEP_STUBS);
        when(resourceLoader.getResource(PRODUCT_PATH).exists()).thenReturn(true);
        validator = new ImportValidator();
        validator.setResourceLoader(resourceLoader);
    }

    @Test
    public void testJobParameters() throws JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString(PARAM_INPUT_RESOURCE, PRODUCT_PATH)
            .addString(PARAM_REPORT_RESOURCE, STATISTIC_PATH)
            .toJobParameters();

        JobParameters spy = spy(jobParameters);
        validator.validate(spy);

        verify(spy, times(2)).getParameters();
        verify(spy, times(1)).getString(PARAM_INPUT_RESOURCE);
        verifyNoMoreInteractions(spy);
    }

    @Test(expected = JobParametersInvalidException.class)
    public void testEmptyJobParameters() throws JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();
        validator.validate(jobParameters);
    }

    @Test(expected = JobParametersInvalidException.class)
    public void testMissingJobParameters() throws JobParametersInvalidException {
        JobParameters jobParameters = new JobParametersBuilder()
            .addString(PARAM_INPUT_RESOURCE, PRODUCT_PATH)
            .toJobParameters();

        validator.validate(jobParameters);
    }

}
