package kr.spring.batch.chapter14.test.batch.unit.tasklet;

import kr.spring.batch.chapter14.batch.CleanTasklet;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

/**
 * Tasklet 을 테스트 하기 위해서는 {@link org.springframework.batch.test.MetaDataInstanceFactory#createStepExecution()}을
 * 이용하여 가상의 StepExecution과 ChunkContext 를 생성하여 Tasklet을 실행합니다.
 * ==> 이 방법을 진작에 알았다면 세부 테스트 시에 고생하지 않았을 텐데...
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:48
 */
public class CleanTaskletTest {

    @Test
    public void clean() throws Exception {

        StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
        StepContribution contrib = new StepContribution(stepExecution);
        ChunkContext context = new ChunkContext(new StepContext(stepExecution));
        CleanTasklet clean = new CleanTasklet();

        RepeatStatus status = clean.execute(contrib, context);
        Assertions.assertThat(status).isEqualTo(RepeatStatus.FINISHED);
    }
}
