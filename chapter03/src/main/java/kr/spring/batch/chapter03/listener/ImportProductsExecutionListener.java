package kr.spring.batch.chapter03.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.stereotype.Component;

/**
 * {@link org.springframework.batch.core.JobExecutionListener},
 * {@link org.springframework.batch.core.StepExecutionListener} 를 상속받지 않고, annotation으로 구현합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 11:48
 */
@Slf4j
@Component
public class ImportProductsExecutionListener implements StepExecutionListener {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("스텝 실행 전에 호출되는 리스너의 메소드입니다.");
	}

	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("스텝 완료 후 호출됩니다. StepName=[{}], ExitStatus=[{}]",
		         stepExecution.getStepName(), stepExecution.getExitStatus());

		return ExitStatus.COMPLETED;
	}
}
