package kr.spring.batch.chapter14.config;

import kr.spring.batch.chapter14.domain.Product;
import org.springframework.context.annotation.Configuration;

/**
 * kr.spring.batch.chapter06.test.jpa.JpaHSqlConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 3:12
 */
@Configuration
public class JpaHSqlConfiguration extends HSqlConfigBase {

    @Override
    public String[] getMappedPackageNames() {
        return new String[]{
            Product.class.getPackage().getName()
        };
    }
}
