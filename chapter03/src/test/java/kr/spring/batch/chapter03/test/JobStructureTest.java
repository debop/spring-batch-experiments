package kr.spring.batch.chapter03.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.test.context.ContextConfiguration;

/**
 * kr.spring.batch.chapter03.test.JobStructureTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 1:09
 */
@Slf4j
@ContextConfiguration(classes = { JobStructureConfiguration.class })
//@ContextConfiguration({ "classpath:/spring/infrastructure-job.xml" })
public class JobStructureTest extends AbstractJobStructureTest {

	@Test
	public void delimitedJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
	}
}
