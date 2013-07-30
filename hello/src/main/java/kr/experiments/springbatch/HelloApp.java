package kr.experiments.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class HelloApp {
    public static void main(String[] args) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        JobLauncher jobLauncher = context.getBean(JobLauncher.class);
        Job job = context.getBean("sampleJob", Job.class);
        JobParametersBuilder builder = new JobParametersBuilder();
        JobExecution jobExecution = jobLauncher.run(job, builder.toJobParameters());
        System.out.println(jobExecution.getExitStatus().getExitCode());
    }
}
