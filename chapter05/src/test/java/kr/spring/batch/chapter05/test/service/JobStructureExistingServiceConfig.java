package kr.spring.batch.chapter05.test.service;

import kr.spring.batch.chapter05.AbstractJobConfiguration;
import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.service.ProductService;
import kr.spring.batch.chapter05.service.ProductServiceAdapter;
import kr.spring.batch.chapter05.test.DummyProductItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * kr.spring.batch.chapter05.test.service.JobStructureExistingServiceConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 2:11
 */
@Configuration
@EnableBatchProcessing
@ComponentScan(basePackageClasses = { ProductServiceAdapter.class })
public class JobStructureExistingServiceConfig extends AbstractJobConfiguration {

	@Bean
	@Override
	public TaskExecutor jobTaskExecutor() throws Exception {
		return null;
	}

	@Bean
	public Job importProductsJob(ItemReader<Product> productItemReader) throws Exception {

		Step step = stepBuilders.get("importProductsJob")
		                        .<Product, Product>chunk(100)
		                        .reader(productItemReader)
		                        .writer(productItemWriter())
		                        .build();

		return jobBuilders.get("importProductsJob")
		                  .start(step)
		                  .build();
	}

	@Bean
	public ItemReader<Product> productItemReader(ProductServiceAdapter productServiceAdapter) throws Exception {
		ItemReaderAdapter<Product> reader = new ItemReaderAdapter<Product>();
		reader.setTargetObject(productServiceAdapter);
		reader.setTargetMethod("getProduct");

		return reader;
	}

	@Bean
	public ProductServiceAdapter productServiceAdapter(ProductService productService) throws Exception {
		ProductServiceAdapter adapter = new ProductServiceAdapter();
		adapter.setProductService(productService);
		adapter.afterPropertiesSet();

		return adapter;
	}

	@Bean
	public ItemWriter<Product> productItemWriter() {
		return new DummyProductItemWriter();
	}
}
