package kr.spring.batch.chapter09.test.besteffort;

import kr.spring.batch.chapter09.batch.ShippedOrderWriter;
import kr.spring.batch.chapter09.domain.OrderEntity;
import kr.spring.batch.chapter09.repository.OrderEntityRepository;
import kr.spring.batch.chapter09.test.AbstractJobConfiguration;
import kr.spring.batch.chapter09.test.JpaHSqlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.jms.JmsItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jmx.access.MBeanProxyFactoryBean;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * kr.spring.batch.chapter09.test.besteffort.IdempotencyConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 1:22
 */
@Slf4j
@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackageClasses = { OrderEntityRepository.class })
@Import({ JpaHSqlConfiguration.class })
public class IdempotencyConfiguration extends AbstractJobConfiguration {

    @Bean
    public Job updateInventoryJob() {
        Step step = stepBuilders.get("updateInventoryStep")
                                .<OrderEntity, OrderEntity>chunk(5).readerIsTransactionalQueue()
                                .reader(orderReader())
                                .writer(orderWriter())
                                .build();
        return jobBuilders.get("updateInventoryJob")
                          .start(step)
                          .build();
    }

    @Bean
    public JmsItemReader<OrderEntity> orderReader() {
        JmsItemReader<OrderEntity> reader = new JmsItemReader<OrderEntity>();
        reader.setJmsTemplate(jmsTemplate());
        reader.setItemType(OrderEntity.class);
        // reader.setItemType(javax.jms.Message.class);

        return reader;
    }

    @Bean
    public ShippedOrderWriter orderWriter() {
        return new ShippedOrderWriter();
    }

//    @Bean
//    @SuppressWarnings("unchecked")
//    public ItemReader<Message> mockReader() {
//        return (ItemReader<Message>) Mockito.mock(ItemReader.class);
//    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jms = new JmsTemplate();
        jms.setConnectionFactory(connectionFactory());
        jms.setDefaultDestination(shippedOrderQueue());
        jms.setReceiveTimeout(100L);
        jms.setSessionTransacted(true);

        return jms;
    }


    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory ccf = new CachingConnectionFactory();
        ccf.setTargetConnectionFactory(new ActiveMQConnectionFactory("vm://embedded?broker.persistent=false"));
        return ccf;
    }


    @Bean
    public ActiveMQQueue shippedOrderQueue() {
        return new ActiveMQQueue("spring.batch.queue.shipped.order");
    }

    //
    // HINT: http://java.dzone.com/articles/managing-activemq-jmx-apis
    // HINT: http://icodingclub.blogspot.kr/2011/09/spring-jms-with-embeded-activemq-in.html
    @Bean
    public QueueViewMBean shippedOrderQueueView() {
        MBeanProxyFactoryBean bean = new MBeanProxyFactoryBean();
        try {
            bean.setProxyInterface(org.apache.activemq.broker.jmx.QueueViewMBean.class);
            bean.setObjectName(new ObjectName("org.apache.activemq:BrokerName=embedded,Type=Queue,Destination=spring.batch.queue.shipped.order"));
            bean.afterPropertiesSet();
        } catch (MalformedObjectNameException e) {
            log.error("에러", e);
        }
        bean.prepare();
        return (QueueViewMBean) bean.getObject();
    }
}
