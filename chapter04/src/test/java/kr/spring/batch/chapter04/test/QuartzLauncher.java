package kr.spring.batch.chapter04.test;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Date;

/**
 * kr.spring.batch.chapter04.test.QuartzLauncher
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 22. 오후 3:54
 */
@Slf4j
public class QuartzLauncher implements org.quartz.Job {

    @Setter
    private Job job;

    @Setter
    private JobLauncher jobLauncher;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobParameters jobParameters = createJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            throw new JobExecutionException("스프링 배치 잡 중에 예외가 발생했습니다.", e);
        }

    }

    private JobParameters createJobParameters() {
        JobParameters jobParams = new JobParametersBuilder().addDate("date", new Date())
                                                            .toJobParameters();
        return jobParams;
    }
}
