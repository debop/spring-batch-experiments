package kr.experiments.springbatch.chapter03.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParameters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.experiments.springbatch.chapter03.test.JobStructureTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 1:09
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobStructureConfiguration.class })
public class JobStructureTest extends AbstractJobStructureTest {

    @Test
    public void delimitedJob() throws Exception {
        jobLauncher.run(job, new JobParameters());
    }
}
