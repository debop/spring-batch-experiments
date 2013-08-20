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
        // Job 전체를 생성하지 않고, 특정 Step만을 Dummy로 생성하여 테스트 할 수 있다.

        stepExecution = MetaDataInstanceFactory.createStepExecution();
        jobExecution = MetaDataInstanceFactory.createJobExecution();
        decider = new NextDecider();
    }

    @Test
    public void testNextStatus() {
        // Step 에서 Write 작업이 5번 일어났다면...
        stepExecution.setWriteCount(5);
        FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
        assertThat(status.getName()).isEqualTo("NEXT");
    }

    @Test
    public void testCompletedStatus() {
        // Step에서 Write 작업이 한번도 일어나지 않았다면... 더 이상 할 일이 없다...
        stepExecution.setWriteCount(0);
        FlowExecutionStatus status = decider.decide(jobExecution, stepExecution);
        assertThat(status).isEqualTo(FlowExecutionStatus.COMPLETED);
    }
}
