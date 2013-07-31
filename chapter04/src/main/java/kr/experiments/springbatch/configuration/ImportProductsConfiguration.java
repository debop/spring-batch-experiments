package kr.experiments.springbatch.configuration;

import kr.experiments.springbatch.chapter04.HelloTasklet;
import kr.experiments.springbatch.chapter04.ModifyExitStatusListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * kr.experiments.springbatch.configuration.ImportProductsConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:34
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@ComponentScan(basePackageClasses = { ModifyExitStatusListener.class })
@Import(LaunchConfiguration.class)
public class ImportProductsConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet helloTasklet() {
        return new HelloTasklet();
    }

    @Bean
    public Step importProductStep() {
        return stepBuilderFactory.get("importProductStep")
                                 .tasklet(helloTasklet())
                                 .build();
    }

    @Bean
    public Job importProductJob() {
        return jobBuilderFactory.get("importProductJob")
                                .start(importProductStep())
                                .build();
    }
}
