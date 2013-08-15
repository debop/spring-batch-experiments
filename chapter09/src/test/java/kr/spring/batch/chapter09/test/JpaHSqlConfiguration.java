package kr.spring.batch.chapter09.test;

import kr.spring.batch.chapter09.domain.Order;
import kr.spring.batch.chapter09.jpa.config.HSqlConfigBase;
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
		return new String[] {
				Order.class.getPackage().getName()
		};
	}
}
