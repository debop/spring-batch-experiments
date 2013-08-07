package kr.spring.batch.chapter06.test.file.delimited;

import kr.spring.batch.chapter06.test.file.AssertLine;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter06.test.file.delimited.JobDelimitedPassThroughFieldExtractorFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 1:25
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobDelimitedPassThroughFieldExtractorFlatFileConfiguration.class })
public class JobDelimitedPassThroughFieldExtractorFlatFileTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void delimitedJob() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        Resource output = new FileSystemResource(JobDelimitedPassThroughFieldExtractorFlatFileConfiguration.OUTPUT_FILE);
        AssertLine.assertLineFileEquals(output, 1, "Product [id=PR....210, name=BlackBerry 8100 Pearl]");
        AssertLine.assertLineFileEquals(output, 7, "Product [id=PR....216, name=AT&T 8525 PDA]");
        AssertLine.assertLineFileEquals(output, 8, "Product [id=PR....217, name=Canon Digital Rebel XT 8MP]");
    }
}
