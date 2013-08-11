package kr.spring.batch.chapter06.advanced;

import kr.spring.batch.chapter06.Product;
import org.springframework.batch.support.annotation.Classifier;

/**
 * 분류자를 이용하여, 레코드별로 작업 종류를 달리 할 수 있다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 3:53
 */
public class ProductRouterClassifier {

	@Classifier
	public String classify(Product classifiable) {
		return classifiable.getOperation();
	}
}
