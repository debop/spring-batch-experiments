package kr.experiments.springbatch.chapter04;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.launch.support.ExitCodeMapper;
import org.springframework.stereotype.Component;

/**
 * kr.experiments.springbatch.chapter04.SkippedAwareExitCodeMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:47
 */
@Component
public class SkippedAwareExitCodeMapper implements ExitCodeMapper {

	@Override
	public int intValue(String exitCode) {
		if (ExitStatus.COMPLETED.getExitCode().equals(exitCode)) {
			return 0;
		} else if (ExitStatus.FAILED.getExitCode().equals(exitCode)) {
			return 1;
		} else if ("COMPLETED WITH SKIPS".equals(exitCode)) {
			return 3;
		} else {
			return 2;
		}

	}
}
