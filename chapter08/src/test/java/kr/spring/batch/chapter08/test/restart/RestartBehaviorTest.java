package kr.spring.batch.chapter08.test.restart;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Iterator;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter08.test.restart.RestartBehaviorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 14. 오후 1:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RestartBehaviorConfiguration.class })
public class RestartBehaviorTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job notRestartableJob;

    @Autowired
    private Tasklet taskletForNotRestartableJob;


    @Test
    public void notRestartable() throws Exception {
        when(taskletForNotRestartableJob.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenThrow(new RuntimeException());

        JobParameters jobParameters =
            new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

        JobExecution exec = jobLauncher.run(notRestartableJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);
        try {
            exec = jobLauncher.run(notRestartableJob, jobParameters);
            Assert.fail("재시작 불가 Job입니다. 재시작을 요청했으므로 예외가 발생해야 합니다.");
        } catch (JobRestartException e) {
            // OK
        }
    }

    @Autowired
    private Job restartableJob;

    @Autowired
    private Tasklet taskletForRestartableJob;

    @Test
    public void restartable() throws Exception {
        when(taskletForRestartableJob.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenThrow(new RuntimeException())
            .thenReturn(RepeatStatus.FINISHED);

        JobParameters jobParameters =
            new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

        JobExecution exec = jobLauncher.run(restartableJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);

        exec = jobLauncher.run(restartableJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        try {
            exec = jobLauncher.run(restartableJob, jobParameters);
            Assert.fail("job 인스턴스는 이미 완료되었습니다. 완료된 놈을 실행시키면 JobInstanceAlreadyCompleteException이 발생해야 합니다.");
        } catch (JobInstanceAlreadyCompleteException e) {
            // OK
        }
    }

    @Autowired
    private Job importProductsJob;

    @Autowired
    private Tasklet decompressTasklet;

    @Autowired
    private Tasklet readWriteProductsTasklet;

    @Test
    public void allowStartIfCompleteTest() throws Exception {
        when(decompressTasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenReturn(RepeatStatus.FINISHED)
            .thenReturn(RepeatStatus.FINISHED);

        when(readWriteProductsTasklet.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenThrow(RuntimeException.class)
            .thenReturn(RepeatStatus.FINISHED);

        JobParameters jobParameters =
            new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

        JobExecution exec = jobLauncher.run(importProductsJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);

        Iterator<StepExecution> iterator = exec.getStepExecutions().iterator();
        StepExecution firstStepExec = iterator.next();
        StepExecution secondStepExec = iterator.next();

        assertThat(firstStepExec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        assertThat(secondStepExec.getExitStatus().getExitCode()).isEqualTo(ExitStatus.FAILED.getExitCode());

        exec = jobLauncher.run(importProductsJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        verify(decompressTasklet, times(2)).execute(any(StepContribution.class), any(ChunkContext.class));
        verify(readWriteProductsTasklet, times(2)).execute(any(StepContribution.class), any(ChunkContext.class));
    }

    @Autowired
    private Job importProductsLimitJob;

    @Autowired
    Tasklet decompressTaskletLimit;

    @Autowired
    Tasklet readWriteProductsTaskletLimit;

    @Test
    public void startLimit() throws Exception {
        when(decompressTaskletLimit.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenReturn(RepeatStatus.FINISHED);

        when(readWriteProductsTaskletLimit.execute(any(StepContribution.class), any(ChunkContext.class)))
            .thenThrow(RuntimeException.class)
            .thenThrow(RuntimeException.class)
            .thenThrow(RuntimeException.class);

        JobParameters jobParameters =
            new JobParametersBuilder().addLong("date", System.currentTimeMillis()).toJobParameters();

        JobExecution exec = jobLauncher.run(importProductsLimitJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);

        exec = jobLauncher.run(importProductsLimitJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);
        assertThat(exec.getStepExecutions().size()).isEqualTo(1);

        exec = jobLauncher.run(importProductsLimitJob, jobParameters);
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.FAILED);
        assertThat(exec.getStepExecutions().size()).isEqualTo(1);

        exec = jobLauncher.run(importProductsLimitJob, jobParameters);
        assertThat(exec.getStepExecutions().size()).isEqualTo(0);

        verify(decompressTaskletLimit, times(1)).execute(any(StepContribution.class), any(ChunkContext.class));
        verify(readWriteProductsTaskletLimit, times(3)).execute(any(StepContribution.class), any(ChunkContext.class));

    }
}
