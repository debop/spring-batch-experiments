package kr.spring.batch.chapter10.listener;

import kr.spring.batch.chapter10.batch.BatchService;
import lombok.Setter;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * kr.spring.batch.chapter10.listener.FileExistsStepListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 3:11
 */
public class FileExistsStepListener implements StepExecutionListener {

	@Setter BatchService batchService;
	@Setter String targetFile;

	@Override
	public void beforeStep(StepExecution stepExecution) {}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (batchService.exists(targetFile)) {
			return new ExitStatus("FILE EXISTS");
		}
		return new ExitStatus("NO FILE");
	}
}
