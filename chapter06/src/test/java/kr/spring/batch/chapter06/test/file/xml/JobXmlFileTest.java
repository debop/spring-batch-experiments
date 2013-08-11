package kr.spring.batch.chapter06.test.file.xml;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.XMLAssert;
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
 * kr.spring.batch.chapter06.test.file.xml.JobXmlFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 2:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobXmlFileConfiguration.class })
public class JobXmlFileTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testXml() throws Exception {
		JobExecution exec = jobLauncherTestUtils.launchJob();
		Assertions.assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		Resource output = new FileSystemResource(JobXmlFileConfiguration.OUTPUT_FILE);
		String content = IOUtils.toString(output.getInputStream());
		XMLAssert.assertXpathEvaluatesTo("8", "count(//product)", content);
		XMLAssert.assertXpathEvaluatesTo("Sony Ericsson W810i", "//product[2]/name", content);
	}
}
