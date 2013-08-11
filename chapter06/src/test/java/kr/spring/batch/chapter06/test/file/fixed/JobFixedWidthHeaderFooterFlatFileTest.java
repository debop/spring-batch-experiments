package kr.spring.batch.chapter06.test.file.fixed;

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

import java.text.DecimalFormatSymbols;

/**
 * kr.spring.batch.chapter06.test.file.fixed.JobFixedWidthHeaderFooterFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 1:58
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobFixedWidthHeaderFooterFlatFileConfiguration.class })
public class JobFixedWidthHeaderFooterFlatFileTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	char decimalSeparator = DecimalFormatSymbols.getInstance().getDecimalSeparator();

	@Test
	public void delimitedJob() throws Exception {
		JobExecution exec = jobLauncherTestUtils.launchJob();
		Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		Resource output = new FileSystemResource(JobFixedWidthHeaderFooterFlatFileConfiguration.OUTPUT_FILE);
		AssertLine.assertLineFileEquals(output, 2, "PR....210124" + decimalSeparator + "60BlackBerry 8100 Pearl         ");
		AssertLine.assertLineFileEquals(output, 8, "PR....216289" + decimalSeparator + "20AT&T 8525 PDA                 ");
		AssertLine.assertLineFileEquals(output, 9, "PR....217 13" + decimalSeparator + "70Canon Digital Rebel XT 8MP    ");
	}
}
