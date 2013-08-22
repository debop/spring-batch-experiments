package kr.spring.batch.chapter04.test.web;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;

/**
 * kr.spring.batch.chapter04.test.web.JobLauncherController
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 6:54
 */
@Controller
public class JobLauncherController {

    private JobLauncher jobLauncher;
    private JobRegistry jobRegistry;

    public JobLauncherController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
        this.jobLauncher = jobLauncher;
        this.jobRegistry = jobRegistry;
    }
}
