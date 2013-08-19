package kr.spring.batch.chapter10.test;

import kr.spring.batch.chapter10.decider.FileExistsDecider;
import kr.spring.batch.chapter10.decider.SkippedItemsDecider;
import kr.spring.batch.chapter10.tasklet.DecompressTasklet;
import kr.spring.batch.chapter10.tasklet.DownloadTasklet;
import kr.spring.batch.chapter10.tasklet.VerifyTasklet;
import kr.spring.batch.config.AbstractJobConfiguration;
import kr.spring.batch.config.TaskletDefinitionConfiguration;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter10.test.JobWithDecidersTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobWithDecidersWithJavaConfigTest.JobWithDecidersCfg.class })
public class JobWithDecidersWithJavaConfigTest extends AbstractJobTest {

    @Configuration
    @EnableBatchProcessing
    @Import({ TaskletDefinitionConfiguration.class })
    public static class JobWithDecidersCfg extends AbstractJobConfiguration {

        @Autowired
        DownloadTasklet downloadTasklet;

        @Autowired
        DecompressTasklet decompressTasklet;

        @Autowired
        VerifyTasklet verifyTasklet;

        @Bean
        public Job importProuctsJob() {

        }

        @Bean
        public Step downloadStep() {
            return stepBuilders.get("downloadStep").tasklet(downloadTasklet).build();
        }

        @Bean
        public FileExistsDecider someFileDownloadedDecision() {
            return new FileExistsDecider();
        }

        @Bean
        public Step decompressStep() {
            return stepBuilders.get("decompressStep").tasklet(decompressTasklet).build();
        }

        @Bean
        public Step verifyStep() {
            return stepBuilders.get("verifyStep").tasklet(verifyTasklet).build();
        }

        @Bean
        public SkippedItemsDecider readWriteStep() {
            return new SkippedItemsDecider();
        }

        @Bean
        public Step generateReportStep() {

        }
    }
}
