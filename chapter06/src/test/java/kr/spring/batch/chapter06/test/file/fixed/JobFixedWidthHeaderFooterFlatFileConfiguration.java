package kr.spring.batch.chapter06.test.file.fixed;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.file.ProductFooterCallback;
import kr.spring.batch.chapter06.file.ProductHeaderCallback;
import kr.spring.batch.chapter06.test.AbstractBatchConfiguration;
import kr.spring.batch.chapter06.test.FlatFileReaderConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;

/**
 * kr.spring.batch.chapter06.test.file.delimited.JobDelimitedBeanWrapperFieldExtractorFlatFileConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 1:18
 */
@Configuration
@EnableBatchProcessing
@Import({ FlatFileReaderConfiguration.class })
public class JobFixedWidthHeaderFooterFlatFileConfiguration extends AbstractBatchConfiguration {

	public static final String OUTPUT_FILE = "target/outputs/fixedwidth-headerfooter.txt";

	@Autowired
	FlatFileItemReader<Product> productItemReader;

	@Bean
	public Job writeProductJob() {
		Step step = stepBuilders.get("readWrite")
		                        .<Product, Product>chunk(10)
		                        .reader(productItemReader)
		                        .writer(productItemWriter())
				.listener(footerCallback())         // HINT: stepExecution을 조회하기 위해!!!
				.build();

		return jobBuilders.get("writeProductJob")
		                  .start(step)
		                  .build();
	}

	@Bean
	public FlatFileItemWriter<Product> productItemWriter() {
		FlatFileItemWriter<Product> writer = new FlatFileItemWriter<Product>();
		writer.setResource(new FileSystemResource(OUTPUT_FILE));
		writer.setHeaderCallback(headerCallback());
		writer.setFooterCallback(footerCallback());
		writer.setLineAggregator(lineAggregator());

		return writer;
	}

	@Bean
	public FormatterLineAggregator<Product> lineAggregator() {

		BeanWrapperFieldExtractor<Product> extractor = new BeanWrapperFieldExtractor<Product>();
		extractor.setNames(new String[] { "id", "price", "name" });

		FormatterLineAggregator<Product> lineAggregator = new FormatterLineAggregator<Product>();
		lineAggregator.setFieldExtractor(extractor);
		lineAggregator.setFormat("%-9s%6.2f%-30s");

		return lineAggregator;
	}

	@Bean
	public ProductHeaderCallback headerCallback() {
		return new ProductHeaderCallback();
	}

	@Bean
	public ProductFooterCallback footerCallback() {
		return new ProductFooterCallback();
	}
}
