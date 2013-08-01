package kr.experiments.springbatch.test.stop;

import kr.experiments.springbatch.AbstractJobConfiguration;
import kr.experiments.springbatch.SpringLaunchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * kr.experiments.springbatch.test.stop.StopConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오전 9:18
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableMBeanExport
@Import({ SpringLaunchConfiguration.class })
@ComponentScan(basePackageClasses = { InfiniteReader.class })
public class StopJobConfiguration extends AbstractJobConfiguration {

    @Autowired
    StopListener stopListener;

    @Autowired
    InfiniteReader reader;

    @Autowired
    EmptyWriter writer;

    @Autowired
    ProcessItemsTasklet tasklet;

    // 종료시키기 위해 PoolSize 를 1 로 설정해야 합니다.
    @Override
    @Bean
    public TaskExecutor taskExecutor() throws Exception {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(1);
        executor.afterPropertiesSet();
        return executor;
    }


    @Bean
    public Job readWriteJob() {
        return jobBuilders.get("readWriteJob")
                          .start(readWriteStep())
                          .build();
    }

    @Bean
    public Step readWriteStep() {
        return stepBuilders.get("readWriteStep")
                           .<String, String>chunk(10)
                           .reader(reader)
                           .writer(writer)
                           .listener((ItemReadListener) stopListener)
                           .listener((StepExecutionListener) stopListener)
                           .build();
    }

    @Bean
    public Job taskletJob() {
        return jobBuilders.get("taskletJob")
                          .start(taskletStep())
                          .build();
    }

    @Bean
    public Step taskletStep() {
        return stepBuilders.get("taskletStep")
                           .tasklet(tasklet)
                           .build();
    }

    @Bean
    public Job jobOperatorJob() {
        return jobBuilders.get("jobOperatorJob")
                          .start(jobOperatorStep())
                          .build();
    }

    @Bean
    public Step jobOperatorStep() {
        return stepBuilders.get("jobOperatorStep")
                           .<String, String>chunk(10)
                           .reader(reader)
                           .writer(writer)
                           .build();
    }

    /*
    @Bean
    public MBeanExporter mBeanExporter() {
        MBeanExporter exporter = new MBeanExporter();
        try {
            Map<String, Object> map = Maps.newHashMap();
            map.put("kr.experiments.springbatch:name=jobOperator", jobOperator);
            exporter.setBeans(map);
        } catch (Exception e) {
            log.info("JobOperator이 이미 등록되었습니다.");
        }
        return exporter;
    }
    */

}
