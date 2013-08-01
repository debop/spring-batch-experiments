package kr.spring.batch.chapter05.test.jpa;

import kr.spring.batch.chapter05.AbstractJobConfiguration;
import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.test.DummyProductItemWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

/**
 * kr.spring.batch.chapter05.test.jpa.JobStructureJpaCursorConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 5:38
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableTransactionManagement
@Import(HSqlConfig.class)
public class JobStructureJpaCursorConfig extends AbstractJobConfiguration {

    @Autowired EntityManagerFactory emf;

    @Override
    public TaskExecutor jobTaskExecutor() throws Exception {
        return null;
    }

//    @Override
//    @Bean(destroyMethod = "close")
//    public DataSource jobDataSource() {
//        return new EmbeddedDatabaseBuilder()
//            .setType(EmbeddedDatabaseType.HSQL)
//            .addScript("classpath:/org/springframework/batch/core/schema-drop-hsqldb.sql")
//            .addScript("classpath:/org/springframework/batch/core/schema-hsqldb.sql")
//            .addScript("classpath:create-tables.sql")
//            .build();
//    }

    @Bean
    public Job importProductsJob() throws Exception {

        Step step = stepBuilders.get("importProductsJob")
                                .<Product, Product>chunk(100)
                                .reader(productItemReader())
                                .writer(productItemWriter())
                                .build();

        return jobBuilders.get("importProductsJob")
                          .start(step)
                          .build();
    }

    @Bean
    public ItemReader<Product> productItemReader() throws Exception {
        JpaPagingItemReader<Product> reader = new JpaPagingItemReader<Product>();
        reader.setEntityManagerFactory(emf);
        reader.setQueryString("select p from Product p");
        reader.setPageSize(100);
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public ItemWriter<Product> productItemWriter() {
        return new DummyProductItemWriter();
    }

//    @Bean
//    public EntityManagerFactory entityManagerFactory() {
//
//        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
//
//        factoryBean.setPackagesToScan(Product.class.getPackage().getName());
//        factoryBean.setDataSource(jobDataSource());
//        factoryBean.setJpaProperties(jpaProperties());
//
//        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setGenerateDdl(true);
//        factoryBean.setJpaVendorAdapter(adapter);
//
//        factoryBean.afterPropertiesSet();
//
//        return factoryBean.getObject();
//    }
//
//    public Properties jpaProperties() {
//        Properties props = new Properties();
//
//        props.put(Environment.FORMAT_SQL, "true");
//        props.put(Environment.SHOW_SQL, "true");
//        props.put(Environment.AUTOCOMMIT, "true");
//        props.put(Environment.STATEMENT_BATCH_SIZE, 100);
//        props.put(Environment.RELEASE_CONNECTIONS, ConnectionReleaseMode.ON_CLOSE);
//
//        props.put(Environment.DIALECT, getDialect());
//
//        return props;
//    }
//
//    public String getDialect() {
//        return "org.hibernate.dialect.HSQLDialect";
//    }
}
