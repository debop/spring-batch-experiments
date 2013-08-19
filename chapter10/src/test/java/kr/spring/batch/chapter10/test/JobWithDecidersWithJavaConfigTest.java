package kr.spring.batch.chapter10.test;

import kr.spring.batch.chapter10.batch.BatchService;
import kr.spring.batch.chapter10.decider.FileExistsDecider;
import kr.spring.batch.chapter10.decider.SkippedItemsDecider;
import kr.spring.batch.chapter10.tasklet.*;
import kr.spring.batch.config.AbstractJobConfiguration;
import kr.spring.batch.config.TaskletDefinitionConfiguration;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
@ContextConfiguration(classes = { JobWithDecidersWithJavaConfigTest.JavaConfiguration.class })
public class JobWithDecidersWithJavaConfigTest extends AbstractJobTest {

    @Configuration
    @EnableBatchProcessing
    @Import({ TaskletDefinitionConfiguration.class })
    public static class JavaConfiguration extends AbstractJobConfiguration {

        @Autowired
        BatchService batchService;

        @Autowired
        DownloadTasklet downloadTasklet;

        @Autowired
        DecompressTasklet decompressTasklet;

        @Autowired
        VerifyTasklet verifyTasklet;

        @Autowired
        Tasklet readWriteProductsTasklet;

        @Autowired
        GenerateReportTasklet generateReportTasklet;

        @Autowired
        Tasklet indexTasklet;

        @Autowired
        TrackImportWithHolderTasklet trackImportWithHolderTasklet;

        @Autowired
        CleanTasklet cleanTasklet;

        @Bean
        public Job importProuctsJob() {
            return jobBuilders.get("importProductsJob")
                              .repository(jobRepository)
                              .start(downloadStep())
                              .next(someFileDownloadedDecision()).on("NO FILE").end().on("FILE EXISTS").to(decompressStep())
                              .next(verifyStep())
                              .next(readWriteStep())
                              .next(skippedItemsDecider()).on("COMPLETED WITH SKIPS").to(generateReportStep()).next(indexStep())
                              .from(skippedItemsDecider()).on("*").to(indexStep())
                              .next(trackImportStep())
                              .next(cleanStep())
                              .build()
                              .build();
        }

        @Bean
        public Step downloadStep() {
            return stepBuilders.get("downloadStep").tasklet(downloadTasklet).build();
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
        public Step readWriteStep() {
            return stepBuilders.get("readWriteStep").tasklet(readWriteProductsTasklet).build();
        }

        @Bean
        public Step generateReportStep() {
            return stepBuilders.get("generateReportStep").tasklet(generateReportTasklet).build();
        }

        @Bean
        public Step indexStep() {
            return stepBuilders.get("indexStep").tasklet(indexTasklet).build();
        }

        @Bean
        public Step trackImportStep() {
            return stepBuilders.get("trackImportStep").tasklet(trackImportWithHolderTasklet).build();
        }

        @Bean
        public Step cleanStep() {
            return stepBuilders.get("cleanStep").tasklet(cleanTasklet).build();
        }

        @Bean
        public FileExistsDecider someFileDownloadedDecision() {
            FileExistsDecider decider = new FileExistsDecider();
            decider.setBatchService(batchService);
            return decider;
        }

        @Bean
        public SkippedItemsDecider skippedItemsDecider() {
            return new SkippedItemsDecider();
        }
    }


}

