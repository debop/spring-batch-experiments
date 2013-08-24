package kr.spring.batch.chapter04.test;

import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * kr.spring.batch.chapter04.test.SpringSchedulingLauncher
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 4:51
 */
@Service
public class SpringSchedulingLauncher {

	@Setter Job job;

	@Setter JobLauncher jobLauncher;

	/**
	 * 1초 단위로 주기적으로 실행하도록 합니다.
	 *
	 * @throws Exception
	 */
	@Scheduled(fixedRate = 1000)
	public void launch() throws Exception {
		JobParameters jobParameters = createJobParameters();
		jobLauncher.run(job, jobParameters);
	}

	private JobParameters createJobParameters() {
		return new JobParametersBuilder()
				.addDate("date", new Date())
				.toJobParameters();
	}
}
