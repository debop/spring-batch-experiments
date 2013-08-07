package kr.spring.batch.chapter06.test.advanced;

import kr.spring.batch.chapter06.Product;
import kr.spring.batch.chapter06.jpa.config.HSqlConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * kr.spring.batch.chapter06.test.jpa.JpaHSqlConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 3:12
 */
@Configuration
@EnableJpaRepositories
public class JpaHSqlConfiguration extends HSqlConfigBase {

    @Override
    public String[] getMappedPackageNames() {
        return new String[]{
            Product.class.getPackage().getName()
        };
    }
}
