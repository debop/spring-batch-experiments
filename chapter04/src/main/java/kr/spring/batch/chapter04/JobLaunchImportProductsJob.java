package kr.spring.batch.chapter04;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

/**
 * kr.spring.batch.chapter04.JobLaunchImportProductsJob
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:33
 */
public class JobLaunchImportProductsJob {

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("/kr/spring/batch/chapter04/import-products-job.xml");

        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job job = context.getBean(Job.class);

        jobLauncher.run(job, new JobParametersBuilder().addString("inputFile", "file:./products.txt")
                                                       .addDate("date", new Date())
                                                       .toJobParameters());
    }
}
