package kr.spring.batch.chapter14.test.batch.unit.tasklet;

import org.junit.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.MetaDataInstanceFactory;

/**
 * kr.spring.batch.chapter14.test.batch.unit.tasklet.CleanTaskletTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:48
 */
public class CleanTaskletTest {

	@Test
	public void clean() throws Exception {
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
	}
}
