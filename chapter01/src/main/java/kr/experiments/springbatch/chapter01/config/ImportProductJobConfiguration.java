package kr.experiments.springbatch.chapter01.config;

import kr.experiments.springbatch.chapter01.batch.DecompressTasklet;
import kr.experiments.springbatch.chapter01.batch.ProductFieldSetMapper;
import kr.experiments.springbatch.chapter01.batch.ProductJdbcItemWriter;
import kr.experiments.springbatch.chapter01.domain.Product;
import kr.experiments.springbatch.config.LaunchConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * kr.experiments.springbatch.chapter01.config.ImportProductConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 3:13
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import(LaunchConfiguration.class)
public class ImportProductJobConfiguration {

	private static final String[] FIELD_NAMES = new String[] { "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" };

	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	@Autowired
	JobBuilderFactory jobBuilders;

	@Autowired
	StepBuilderFactory stepBuilders;

	@Autowired
	JobRepository jobRepository;

	@Autowired
	JobRegistry jobRegistry;

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	DataSource dataSource;

	@Autowired
	JobExecutionListener loggerListener;

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	@StepScope
	public DecompressTasklet decompressTasklet(@Value("#{jobParameters['inputResource']}") String inputResource,
	                                           @Value("#{jobParameters['targetDirectory']}") String targetDirectory,
	                                           @Value("#{jobParameters['targetFile']}") String targetFile) {
		DecompressTasklet tasklet = new DecompressTasklet();
		tasklet.setInputResource(new ClassPathResource(inputResource));
		tasklet.setTargetDirectory(targetDirectory);
		tasklet.setTargetFile(targetFile);
		return tasklet;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Product> reader(@Value("#{jobParameters['targetDirectory'] + jobParameters['targetFile']}") String path) {
		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

		reader.setResource(new FileSystemResource(path));
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());

		return reader;
	}

	@Bean
	@StepScope
	public DefaultLineMapper<Product> lineMapper() {
		DefaultLineMapper<Product> mapper = new DefaultLineMapper<Product>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(FIELD_NAMES);
		mapper.setLineTokenizer(tokenizer);

		ProductFieldSetMapper fieldSetMapper = new ProductFieldSetMapper();
		mapper.setFieldSetMapper(fieldSetMapper);

		return mapper;
	}

	@Bean
	@StepScope
	public ProductJdbcItemWriter writer() {
		return new ProductJdbcItemWriter(dataSource);
	}

	@Bean
	public Job importProductsJob(Tasklet decompressTasklet, ItemReader<Product> reader) {

		Step decompress = stepBuilders.get("decompress")
		                              .tasklet(decompressTasklet)
		                              .repository(jobRepository)
		                              .transactionManager(transactionManager)
		                              .build();

		Step readWriteProducts = stepBuilders.get("readWriteProducts")
		                                     .<Product, Product>chunk(3)
		                                     .reader(reader)
		                                     .writer(writer())
		                                     .faultTolerant()
		                                     .skipLimit(5)
		                                     .skip(FlatFileParseException.class)
		                                     .build();

		return jobBuilders.get("importProductsJob")
		                  .repository(jobRepository)
		                  .listener(loggerListener)
		                  .start(decompress)
		                  .next(readWriteProducts)
		                  .build();
	}
}
