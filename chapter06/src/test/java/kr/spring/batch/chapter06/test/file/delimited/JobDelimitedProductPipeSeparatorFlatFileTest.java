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
 * kr.spring.batch.chapter06.test.file.delimited.JobDelimitedProductPipeSeparatorFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 1:34
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobDelimitedProductPipeSeparatorFlatFileConfiguration.class })
public class JobDelimitedProductPipeSeparatorFlatFileTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void delimitedJob() throws Exception {
        JobExecution exec = jobLauncherTestUtils.launchJob();
        Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        Resource output = new FileSystemResource(JobDelimitedProductPipeSeparatorFlatFileConfiguration.OUTPUT_FILE);
        AssertLine.assertLineFileEquals(output, 1, "PR....210|124.60|BlackBerry 8100 Pearl");
        AssertLine.assertLineFileEquals(output, 7, "PR....216|289.20|AT&T 8525 PDA");
        AssertLine.assertLineFileEquals(output, 8, "PR....217|13.70|Canon Digital Rebel XT 8MP");
    }
}
