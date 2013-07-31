package kr.experiments.springbatch.chapter04;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

/**
 * kr.experiments.springbatch.chapter04.ModifyExitStatusListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:44
 */
@Component
public class ModifyExitStatusListener extends StepExecutionListenerSupport {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobExecution().getJobParameters();
        String exitStatus = jobParameters.getString("exit.status");

        if (exitStatus != null)
            return new ExitStatus(exitStatus);

        return super.afterStep(stepExecution);
    }
}
