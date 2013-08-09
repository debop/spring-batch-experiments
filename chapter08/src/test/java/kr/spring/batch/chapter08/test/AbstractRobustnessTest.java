package kr.spring.batch.chapter08.test;

import kr.spring.batch.chapter08.test.beans.BusinessService;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * kr.spring.batch.chapter08.test.AbstractRobustnessTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:53
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractRobustnessTest {

    @Autowired
    protected JobLauncher jobLauncher;

    @Autowired
    protected Job job;

    @Autowired
    protected BusinessService service;

    @Autowired
    protected SkipListener<?, ?> skipListener;

    @Before
    public void setUp() {
        Mockito.reset(service);
        Mockito.reset(skipListener);
    }


    protected void configureServiceForRead(BusinessService service, int count) {
        List<String> args = new ArrayList<String>();
        for (int i = 2; i <= count; i++)
            args.add(String.valueOf(i));
        args.add(null);
        Mockito.when(service.reading()).thenReturn("1", args.toArray(new String[0]));
    }

    protected void assertRead(int read, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getReadCount()).isEqualTo(read);
    }

    protected void assertWrite(int write, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getWriteCount()).isEqualTo(write);
    }

    protected void assertProcessSkip(int processSkip, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getProcessSkipCount()).isEqualTo(processSkip);
    }

    protected void assertReadSkip(int readSkip, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getReadSkipCount()).isEqualTo(readSkip);
    }

    protected void assertWriteSkip(int writeSkip, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getWriteSkipCount()).isEqualTo(writeSkip);
    }

    protected void assertCommit(int commit, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getCommitCount()).isEqualTo(commit);
    }

    protected void assertRollback(int rollback, JobExecution exec) {
        StepExecution stepExec = getStepExecution(exec);
        Assertions.assertThat(stepExec.getRollbackCount()).isEqualTo(rollback);
    }

    protected StepExecution getStepExecution(JobExecution exec) {
        return exec.getStepExecutions().iterator().next();
    }
}
