package kr.spring.batch.chapter14.test.batch.unit.decider;

import kr.spring.batch.chapter14.batch.NextDecider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter14.test.batch.unit.decider.NextDeciderTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 10:38
 */
public class NextDeciderTest {

	StepExecution stepExecution = null;
	JobExecution jobExecution = null;
	NextDecider decider = null;

	@Before
	public void setUp() {
		stepExecution = MetaDataInstanceFactory.createStepExecution();
		jobExecution = MetaDataInstanceFactory.createJobExecution();
		decider = new NextDecider();
	}

	@Test
	public void testNextStatus() {
		stepExecution.setWriteCount(5);
		FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
		assertThat(status.getName()).isEqualTo("NEXT");
	}

	@Test
	public void testCompletedStatus() {
		stepExecution.setWriteCount(0);
		FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
		assertThat(status).isEqualTo(FlowExecutionStatus.COMPLETED);
	}
}
