package kr.spring.batch.chapter06.test.advanced;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.advanced.ProductRepository;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter06.test.advanced.JobClassifierCompositeItemWriterTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 3:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobClassifierCompositeItemWriterConfiguration.class })
public class JobClassifierCompositeItemWriterTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ProductRepository repository;

    @Before
    public void setup() {
        repository.deleteAll();
        repository.flush();

        repository.save(new Product("PR....211", "Sony Ericsson W810i", "", new BigDecimal(139.45), ""));
        repository.save(new Product("PR....212", "Samsung MM-A900M Ace", "", new BigDecimal(97.80), ""));
        repository.flush();
    }

    @Test
    public void testClassifier() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        // 미리 2개 넣고, 8건 추가 시 삭제 1건, Update 1건, 6건 추가 => 7건 남음
        Assertions.assertThat(repository.count()).isEqualTo(7);
    }
}
