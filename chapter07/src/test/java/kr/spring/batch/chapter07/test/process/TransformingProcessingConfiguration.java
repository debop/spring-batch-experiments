package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.*;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import kr.spring.batch.chapter07.test.AbstractBatchConfiguration;
import kr.spring.batch.chapter07.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManagerFactory;

/**
 * kr.spring.batch.chapter07.test.process.TransformingProcessingConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 6:35
 */
@Slf4j
@Configuration
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@EnableBatchProcessing
@Import({ JpaHSqlConfiguration.class })
public class TransformingProcessingConfiguration extends AbstractBatchConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	ItemReader<PartnerProduct> reader;

	@Autowired
	Step readWriteStep;

	@Bean(name = "readWriteJob")
	public Job readWriteJob() {
		return jobBuilders.get("readWriteJob")
		                  .start(readWriteStep)
		                  .build();
	}

	@Bean
	public Step readWriteStep() {
		return stepBuilders.get("readWrite")
		                   .<PartnerProduct, Product>chunk(2)
		                   .reader(reader)
		                   .processor(processor())
		                   .writer(writer())
		                   .build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<PartnerProduct> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
		FlatFileItemReader<PartnerProduct> reader = new FlatFileItemReader<PartnerProduct>();

		reader.setResource(new ClassPathResource(inputFile));
		reader.setLinesToSkip(1);
		reader.setLineMapper(productLineMapper());

		return reader;
	}

	@Bean
	public DefaultLineMapper<PartnerProduct> productLineMapper() {
		DefaultLineMapper<PartnerProduct> lineMapper = new DefaultLineMapper<PartnerProduct>();
		lineMapper.setLineTokenizer(productLineTokenizer());
		lineMapper.setFieldSetMapper(productFieldSetMapper());
		return lineMapper;
	}

	@Bean
	public LineTokenizer productLineTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "PRODUCT_ID", "TITLE", "DETAILS", "PRICE", "RELEASE_DATE" });
		return tokenizer;
	}

	@Bean
	public FieldSetMapper<PartnerProduct> productFieldSetMapper() {
		return new PartnerProductFieldSetMapper();
	}

	@Bean
	public ItemProcessor<PartnerProduct, Product> processor() {
		PartnerProductItemProcessor processor = new PartnerProductItemProcessor();
		processor.setMapper(partnerProductMapper());
		return processor;
	}

	@Bean
	public SimplePartnerProductMapper partnerProductMapper() {
		return new SimplePartnerProductMapper();
	}

	@Bean
	public JpaItemWriter<Product> writer() {
		JpaItemWriter<Product> writer = new JpaItemWriter<Product>();
		writer.setEntityManagerFactory(emf);
		return writer;
	}


	@Bean(name = "readWriteJobPojo")
	public Job readWriteJobPojo() {
		return jobBuilders.get("readWriteJobPojo")
		                  .start(readWriteStepPojo())
		                  .build();
	}

	@Bean
	public Step readWriteStepPojo() {

		return
				stepBuilders.get("readWriteStepPojo")
				            .<PartnerProduct, Product>chunk(2)
				            .reader(reader)
				            .processor(itemProcessorAdapter())
				            .writer(writer())
				            .build();
	}

	@Bean
	public ItemProcessor<PartnerProduct, Product> itemProcessorAdapter() {
		ItemProcessorAdapter<PartnerProduct, Product> adapter = new ItemProcessorAdapter<>();
		adapter.setTargetObject(partnerProductMapper());
		adapter.setTargetMethod("map");

		return adapter;
	}

}
