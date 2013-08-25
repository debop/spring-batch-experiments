package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.PartnerIdItemProcessor;
import kr.spring.batch.chapter07.PartnerIdMapper;
import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.ProductFieldSetMapper;
import kr.spring.batch.chapter07.jpa.PartnerMappingRepository;
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
 * kr.spring.batch.chapter07.test.process.ItemProcessorConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:26
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class ChangingStateProcessingConfiguration extends AbstractBatchConfiguration {

	@Autowired
	EntityManagerFactory emf;

	@Autowired
	PartnerMappingRepository partnerMappingRepository;

	@Autowired
	ItemReader<Product> reader;

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
		                   .<Product, Product>chunk(2)
		                   .reader(reader)
		                   .processor(processor())
		                   .writer(writer())
		                   .build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Product> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {
		FlatFileItemReader<Product> reader = new FlatFileItemReader<Product>();

		reader.setResource(new ClassPathResource(inputFile));
		reader.setLinesToSkip(1);
		reader.setLineMapper(productLineMapper());

		return reader;
	}

	@Bean
	public DefaultLineMapper<Product> productLineMapper() {
		DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<Product>();
		lineMapper.setLineTokenizer(productLineTokenizer());
		lineMapper.setFieldSetMapper(productFieldSetMapper());
		return lineMapper;
	}

	@Bean
	public LineTokenizer productLineTokenizer() {
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
		tokenizer.setNames(new String[] { "PRODUCT_ID", "NAME", "DESCRIPTION", "PRICE" });
		return tokenizer;
	}

	@Bean
	public FieldSetMapper<Product> productFieldSetMapper() {
		return new ProductFieldSetMapper();
	}

	@Bean
	public ItemProcessor<Product, Product> processor() {
		PartnerIdItemProcessor processor = new PartnerIdItemProcessor();
		processor.setMapper(partnerIdMapper());
		return processor;
	}

	@Bean
	public PartnerIdMapper partnerIdMapper() {
		PartnerIdMapper mapper = new PartnerIdMapper();
		mapper.setPartnerId("PARTNER1");
		mapper.setPartnerMappingRepository(partnerMappingRepository);
		return mapper;
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
				            .<Product, Product>chunk(2)
				            .reader(reader)
				            .processor(itemProcessorAdapter())
				            .writer(writer())
				            .build();
	}

	@Bean
	public ItemProcessor<Product, Product> itemProcessorAdapter() {
		ItemProcessorAdapter<Product, Product> adapter = new ItemProcessorAdapter<>();
		adapter.setTargetObject(partnerIdMapper());
		adapter.setTargetMethod("map");

		return adapter;
	}
}
