package kr.experiments.springbatch.chapter02.structure;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.experiments.springbatch.chapter02.structure.JobStructureSimpleTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 10:26
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobStructureSimpleConfiguration.class })
public class JobStructureSimpleTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Test
    public void simpleJob() throws Exception {
        log.info("Job executing...");
        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());

        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        for(StepExecution stepExecution: jobExecution.getStepExecutions()) {
            log.debug("Step=[{}], ExitStatus=[{}]", stepExecution.getStepName(), stepExecution.getExitStatus());
            assertThat(stepExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        }
    }
}
