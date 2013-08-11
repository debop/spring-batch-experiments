package kr.experiments.springbatch.chapter04;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Map;

/**
 * kr.experiments.springbatch.chapter04.HelloTasklet
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:27
 */
@Slf4j
public class HelloTasklet implements Tasklet {

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();

		if (jobParameters.isEmpty()) {
			log.info("No job parameters!");
		} else {
			log.info("Job parameters:");
			for (Map.Entry<String, JobParameter> p : jobParameters.getParameters().entrySet()) {
				log.info("[{}]=[{}] ({})", p.getKey(), p.getValue().getValue(), p.getValue().getType());
			}
		}
		return RepeatStatus.FINISHED;
	}
}
