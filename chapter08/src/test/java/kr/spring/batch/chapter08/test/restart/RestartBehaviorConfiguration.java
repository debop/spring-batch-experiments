package kr.spring.batch.chapter08.test.restart;

import kr.spring.batch.chapter08.jpa.repositories.ProductRepository;
import kr.spring.batch.chapter08.test.AbstractRobustnessJobConfiguration;
import kr.spring.batch.chapter08.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * kr.spring.batch.chapter08.test.restart.RestartBehaviorConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 14. 오후 1:29
 */
@Slf4j
@Configuration
@EnableAspectJAutoProxy
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class RestartBehaviorConfiguration extends AbstractRobustnessJobConfiguration {

    @Bean
    public Job notRestartableJob() {
        Step step = stepBuilders.get("notStartableStep").tasklet(taskletForNotRestartableJob()).build();
        return jobBuilders.get("notStartableJob")
                          .start(step)
            .preventRestart()     // NOTE 재시작 금지
            .build();
    }

    @Bean
    public Tasklet taskletForNotRestartableJob() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public Job restartableJob() {
        Step step = stepBuilders.get("restartableJob").tasklet(taskletForRestartableJob()).build();
        return jobBuilders.get("restartableJob")
                          .start(step)
                          .build();
    }

    @Bean
    public Tasklet taskletForRestartableJob() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public Job importProductsJob() {
        Step decompressStep = stepBuilders.get("decompressStep")
                                          .tasklet(decompressTasklet())
                                          .allowStartIfComplete(true)
                                          .build();

        Step readWriteProductsStep = stepBuilders.get("readWriteProductsStep")
                                                 .tasklet(readWriteProductsTasklet())
                                                 .build();

        return jobBuilders.get("importProductsJob")
                          .start(decompressStep)
                          .next(readWriteProductsStep)
                          .build();
    }

    @Bean
    public Tasklet decompressTasklet() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public Tasklet readWriteProductsTasklet() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public Job importProductsLimitJob() {
        Step decompressStepLimit = stepBuilders.get("decompressStepLimit")
                                               .tasklet(decompressTaskletLimit())
                                               .build();

        // NOTE: Start Limit is 3
        Step readWriteProductsStepLimit = stepBuilders.get("readWriteProductsStepLimit")
                                                      .tasklet(readWriteProductsTaskletLimit())
                                                      .startLimit(3)
                                                      .build();

        return jobBuilders.get("importProductsLimitJob")
                          .start(decompressStepLimit)
                          .next(readWriteProductsStepLimit)
                          .build();
    }

    @Bean
    public Tasklet decompressTaskletLimit() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public Tasklet readWriteProductsTaskletLimit() {
        return Mockito.mock(Tasklet.class);
    }
}
