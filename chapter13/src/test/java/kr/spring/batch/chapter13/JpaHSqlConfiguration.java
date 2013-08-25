package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.Product;
import kr.spring.batch.chapter13.repository.ProductRepository;
import kr.spring.batch.infrastructure.jpa.HSqlConfigBase;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * kr.spring.batch.chapter13.JpaHSqlConfiguration
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 25. 오후 7:22
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = { ProductRepository.class })
public class JpaHSqlConfiguration extends HSqlConfigBase {
	@Override
	public String[] getMappedPackageNames() {
		return new String[] {
				Product.class.getPackage().getName()
		};
	}
}
