package kr.spring.batch.chapter01.test.batch;

import kr.spring.batch.chapter01.batch.DecompressTasklet;
import kr.spring.batch.chapter01.batch.ProductFieldSetMapper;
import kr.spring.batch.chapter01.batch.ProductJdbcItemWriter;
import kr.spring.batch.chapter01.domain.Product;
import kr.spring.batch.infrastructure.BatchDataSourceConfiguration;
import kr.spring.batch.infrastructure.BatchInfrastructureConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.LineMapper;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

/**
 * kr.spring.batch.chapter01.test.batch.ImportProductsConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 22. 오후 8:26
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@Import({ BatchInfrastructureConfiguration.class, BatchDataSourceConfiguration.class })
public class ImportProductsConfiguration {

	@Autowired StepBuilderFactory stepBuilders;
	@Autowired JobBuilderFactory jobBuilders;

	@Autowired DecompressTasklet decompressTasklet;
	@Autowired FlatFileItemReader<String> reader;

	@Bean(name = "dataSource")
	public EmbeddedDatabase dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:create-tables.sql")
				.build();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}


	@Bean
	public Job importProducts() {
		Step step1 = stepBuilders.get("decompress").tasklet(decompressTasklet).build();
		Step step2 = stepBuilders.get("readWriteProducts")
		                         .<String, Product>chunk(3).faultTolerant().skipLimit(5).skip(FlatFileParseException.class)
		                         .reader(reader).writer(writer())
		                         .build();

		return jobBuilders.get("readWriteProducts")
		                  .start(step1)
		                  .next(step2)
		                  .build();
	}

	@Bean
	@StepScope
	public DecompressTasklet decompressTasklet(
			@Value("#{jobParameters['inputResource']}") String inputResource,
			@Value("#{jobParameters['targetDirectory']}") String targetDirectory,
			@Value("#{jobParameters['targetFile']}") String targetFile) throws FileNotFoundException {

		DecompressTasklet tasklet = new DecompressTasklet();
		// tasklet.setInputResource(new FileSystemResource(ResourceUtils.getFile(inputResource)));
		tasklet.setInputResource(new ClassPathResource(inputResource));
		tasklet.setTargetDirectory(targetDirectory);
		tasklet.setTargetFile(targetFile);

		return tasklet;
	}

	@Bean
	@StepScope
	public FlatFileItemReader<String> reader(@Value("#{jobParameters['targetDirectory']}") String targetDirectory,
	                                         @Value("#{jobParameters['targetFile']}") String targetFile) throws FileNotFoundException {
		FlatFileItemReader<String> reader = new FlatFileItemReader<String>();

		String resoueceLocation = targetDirectory + targetFile;
		reader.setResource(new FileSystemResource(ResourceUtils.getFile(resoueceLocation)));
		reader.setLinesToSkip(1);
		reader.setLineMapper(lineMapper());

		return reader;
	}

	@Bean
	public LineMapper lineMapper() {
		DefaultLineMapper lineMapper = new DefaultLineMapper();
		lineMapper.setLineTokenizer(tokenizer());
		lineMapper.setFieldSetMapper(fieldSetMapper());
		return lineMapper;
	}

	@Bean
	public DelimitedLineTokenizer tokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" });
		return tokenizer;
	}

	@Bean
	public ProductFieldSetMapper fieldSetMapper() {
		return new ProductFieldSetMapper();
	}

	@Bean
	public ProductJdbcItemWriter writer() {
		return new ProductJdbcItemWriter(dataSource());
	}
}