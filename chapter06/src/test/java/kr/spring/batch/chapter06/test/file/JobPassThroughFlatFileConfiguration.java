package kr.spring.batch.chapter06.test.file;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.test.AbstractJobConfiguration;
import kr.spring.batch.chapter06.test.FlatFileReaderConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;

/**
 * kr.spring.batch.chapter06.test.file.JobPassThroughFlatFileConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오전 10:51
 */
@Configuration
@EnableBatchProcessing
@Import({ FlatFileReaderConfiguration.class })
public class JobPassThroughFlatFileConfiguration extends AbstractJobConfiguration {

    public static final String OUTPUT_FILE = "target/outputs/passthrough.txt";

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
    public FlatFileItemWriter<Product> productItemWriter() {
        FlatFileItemWriter<Product> writer = new FlatFileItemWriter<Product>();
        writer.setResource(new FileSystemResource(OUTPUT_FILE));
        writer.setLineAggregator(new PassThroughLineAggregator<Product>());

        return writer;
    }
}
