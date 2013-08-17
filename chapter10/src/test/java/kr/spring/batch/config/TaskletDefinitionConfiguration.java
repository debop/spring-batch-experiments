package kr.spring.batch.config;

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
 * BUG: Tasklet에 대해 Proxy를 만드는데 예외가 발생한다.
 * BUG: final 이나 private 인 경우 문제가 된다는데... 뭐가 문제인지...
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 17. 오후 1:17
 */
@Configuration
public class TaskletDefinitionConfiguration {

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
	public DecompressTasklet decompressTasklet(@Value("#{jobParameters['archiveFile']}") String archiveFile,
	                                           @Value("#{jobParameters['workingDirectory']}") String workingDirectory) {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setBatchService(batchService());
		tasklet.setInputFile(archiveFile);
		tasklet.setOutputDirectory(workingDirectory);

		return tasklet;
	}

	@Bean
	@StepScope
	public VerifyTasklet verifyTasklet(@Value("#{jobParameters['workingDirectory']}") String workingDirectory) {
		VerifyTasklet tasklet = new VerifyTasklet();
		tasklet.setBatchService(batchService());
		tasklet.setOutputDirectory(workingDirectory);
		tasklet.setImportMetadataHolder(importMetadataHolder());

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
		GenerateReportTasklet tasklet = new GenerateReportTasklet();
		tasklet.setBatchService(batchService());
		return tasklet;
	}

	@Bean
	public Tasklet indexTasklet() {
		return Mockito.mock(Tasklet.class);
	}

	@Bean
	public TrackImportWithHolderTasklet trackImportrTasklet() {
		TrackImportWithHolderTasklet tasklet = new TrackImportWithHolderTasklet();
		tasklet.setBatchService(batchService());
		tasklet.setImportMetadataHolder(importMetadataHolder());

		return tasklet;
	}

	@Bean
	@StepScope
	public CleanTasklet cleanTasklet(@Value("#{jobParameters['workingDirectory']}") String workingDirectory) {
		CleanTasklet tasklet = new CleanTasklet();
		tasklet.setBatchService(batchService());
		tasklet.setOutputDirectory(workingDirectory);

		return tasklet;
	}
}
