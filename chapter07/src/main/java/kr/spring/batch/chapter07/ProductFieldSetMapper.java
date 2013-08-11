package kr.spring.batch.chapter07;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * FieldSet 정보를 읽어 Product 인스턴스를 빌드합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 2:50
 */
@Slf4j
public class ProductFieldSetMapper implements FieldSetMapper<Product> {

	@Override
	public Product mapFieldSet(FieldSet fieldSet) throws BindException {

		log.trace("FieldSet으로 Product를 빌드합니다...");

		// TODO : 나중에 ModelMapper 에서도 잘되나 보자.
		Product product = new Product();

		product.setId(fieldSet.readString("PRODUCT_ID"));
		product.setName(fieldSet.readString("NAME"));
		product.setDescription(fieldSet.readString("DESCRIPTION"));
		product.setPrice(fieldSet.readBigDecimal("PRICE"));

		log.trace("FieldSet으로 Product를 빌드했습니다. product=[{}]", product);

		return product;
	}
}
