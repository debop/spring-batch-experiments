package kr.spring.batch.chapter10.config;

import kr.spring.batch.chapter10.batch.BatchService;
import kr.spring.batch.chapter10.batch.ImportMetadataHolder;
import kr.spring.batch.chapter10.tasklet.*;
import org.mockito.Mockito;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * kr.spring.batch.chapter10.config.TaskletDefinition
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:34
 */
@Configuration
public class TaskletDefinition {

    @Bean
    public BatchService batchService() {
        return Mockito.mock(BatchService.class);
    }

    @Bean
    @StepScope
    public DownloadTasklet downloadTasklet(@Value("#{jobParameters['archiveFile']}") String archiveFile) {
        DownloadTasklet tasklet = new DownloadTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setTargetFile(archiveFile);

        return tasklet;
    }

    @Bean
    @StepScope
    public DecompressTasklet decompressTasklet(@Value("#{jobParameters['archiveFile']}") String inputFile,
                                               @Value("#{jobParameters['workingDirectory']}") String outputDirectory) {
        DecompressTasklet tasklet = new DecompressTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setInputFile(inputFile);
        tasklet.setOutputDirectory(outputDirectory);

        return tasklet;
    }

    @Bean
    @StepScope
    public VerifyTasklet verifyTasklet(@Value("#{jobParameters['workingDirectory']}") String outputDirectory) {
        VerifyTasklet tasklet = new VerifyTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setOutputDirectory(outputDirectory);
        tasklet.setImportMetadataHolder(importMetadataHolder());

        return tasklet;
    }

    @Bean
    @StepScope
    public VerifyStoreInJobContextTasklet verifyStoreInJobContextTasklet(
        @Value("#{jobParameters['workingDirectory']}") String outputDirectory) {
        VerifyStoreInJobContextTasklet tasklet = new VerifyStoreInJobContextTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setOutputDirectory(outputDirectory);

        return tasklet;
    }

    @Bean
    @StepScope
    public VerifyStoreInStepContextTasklet verifyStoreInStepContextTasklet(
        @Value("#{jobParameters['workingDirectory']}") String outputDirectory) {
        VerifyStoreInStepContextTasklet tasklet = new VerifyStoreInStepContextTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setOutputDirectory(outputDirectory);

        return tasklet;
    }

    @Bean
    public ImportMetadataHolder importMetadataHolder() {
        return new ImportMetadataHolder();
    }

    @Bean
    public Tasklet readWriteProductsTasklet() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public GenerateReportTasklet generateReportTasklet() {
        return new GenerateReportTasklet(batchService());
    }

    @Bean
    public Tasklet indexTasklet() {
        return Mockito.mock(Tasklet.class);
    }

    @Bean
    public TrackImportWithHolderTasklet trackImportTasklet() {
        TrackImportWithHolderTasklet tasklet = new TrackImportWithHolderTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setImportMetadataHolder(importMetadataHolder());
        return tasklet;
    }

    @Bean
    @StepScope
    public CleanTasklet cleanTasklet(@Value("#{jobParameters['workingDirectory']}") String outputDirectory) {
        CleanTasklet tasklet = new CleanTasklet();
        tasklet.setBatchService(batchService());
        tasklet.setOutputDirectory(outputDirectory);

        return tasklet;
    }
}
