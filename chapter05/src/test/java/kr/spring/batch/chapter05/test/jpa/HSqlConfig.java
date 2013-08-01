package kr.spring.batch.chapter05.test.jpa;

import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.jpa.config.HSqlConfigBase;
import kr.spring.batch.chapter05.jpa.repository.ProductRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * kr.spring.batch.chapter05.test.jpa.HSqlConfig
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 5:59
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
public class HSqlConfig extends HSqlConfigBase {

    @Override
    public String[] getMappedPackageNames() {
        return new String[]{
            Product.class.getPackage().getName()
        };
    }
}
