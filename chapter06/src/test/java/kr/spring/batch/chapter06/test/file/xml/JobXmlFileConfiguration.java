package kr.spring.batch.chapter06.test.file.xml;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.test.AbstractBatchConfiguration;
import kr.spring.batch.chapter06.test.FlatFileReaderConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;

/**
 * kr.spring.batch.chapter06.test.file.xml.JobXmlFileConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 2:06
 */
@Configuration
@EnableBatchProcessing
@Import({ FlatFileReaderConfiguration.class })
public class JobXmlFileConfiguration extends AbstractBatchConfiguration {

	public static final String OUTPUT_FILE = "target/outputs/products.xml";

	@Autowired
	FlatFileItemReader<Product> productItemReader;

	@Bean
	public Job writeProductJob() {
		Step step = stepBuilders.get("readWrite")
		                        .<Product, Product>chunk(10)
		                        .reader(productItemReader)
		                        .writer(productItemWriter())
		                        .build();

		return jobBuilders.get("writeProductJob")
		                  .start(step)
		                  .build();
	}

	@Bean
	public StaxEventItemWriter<Product> productItemWriter() {
		StaxEventItemWriter<Product> writer = new StaxEventItemWriter<Product>();
		writer.setResource(new FileSystemResource(OUTPUT_FILE));
		writer.setMarshaller(productMarshaller());
		writer.setRootTagName("products");
		writer.setOverwriteOutput(true);

		return writer;
	}

	@Bean
	public XStreamMarshaller productMarshaller() {

		HashMap<String, Class> aliases = new HashMap<String, Class>();
		aliases.put("product", Product.class);

		XStreamMarshaller marshaller = new XStreamMarshaller();
		try {
			marshaller.setAliases(aliases);
		} catch (Exception ignored) {}

		return marshaller;
	}
}
