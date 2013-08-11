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
 * kr.spring.batch.chapter06.test.file.delimited.JobDelimitedProductFieldExtractorFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 1:30
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobDelimitedProductFieldExtractorFlatFileConfiguration.class })
public class JobDelimitedProductFieldExtractorFlatFileTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void delimitedJob() throws Exception {
		JobExecution exec = jobLauncherTestUtils.launchJob();
		Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		Resource output = new FileSystemResource(JobDelimitedProductFieldExtractorFlatFileConfiguration.OUTPUT_FILE);
		AssertLine.assertLineFileEquals(output, 1, "BEGIN,PR....210,124.60,18.6900,BlackBerry 8100 Pearl,END");
		AssertLine.assertLineFileEquals(output, 7, "BEGIN,PR....216,289.20,43.3800,AT&T 8525 PDA,END");
		AssertLine.assertLineFileEquals(output, 8, "BEGIN,PR....217,13.70,2.0550,Canon Digital Rebel XT 8MP,END");
	}
}
