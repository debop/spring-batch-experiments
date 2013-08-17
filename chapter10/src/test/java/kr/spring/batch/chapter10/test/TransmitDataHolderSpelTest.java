package kr.spring.batch.chapter10.test;

import kr.spring.batch.config.AbstractJobConfiguration;
import org.junit.runner.RunWith;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter10.test.TransmitDataHolderSpelTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 17. 오전 11:19
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration({ "classpath:kr/spring/batch/chapter10/batch-infrastructure.xml",
//		                      "classpath:kr/spring/batch/chapter10/transmit-data-holder-spel-job.xml" })
@ContextConfiguration(classes = { TransmitDataHolderSpelTest.JobConfiguration.class })
public class TransmitDataHolderSpelTest extends AbstractJobTest {

	@Configuration
	@EnableBatchProcessing
	@ImportResource({ "classpath:kr/spring/batch/chapter10/transmit-data-holder-spel-job.xml" })
	public static class JobConfiguration extends AbstractJobConfiguration {
	}
}
