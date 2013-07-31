package kr.experiments.springbatch.test.web;

import lombok.Setter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import java.util.Date;

/**
 * kr.experiments.springbatch.test.web.SimpleLauncher
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 6:45
 */
public class SimpleLauncher {

    @Setter private JobLauncher jobLauncher;
    @Setter private Job job;

    public void launch() throws Exception {
        jobLauncher.run(job, new JobParametersBuilder().addDate("date", new Date()).toJobParameters());
    }
}
