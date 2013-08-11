package kr.spring.batch.chapter07;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * kr.spring.batch.chapter07.PartnerProductFieldSetMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:11
 */
public class PartnerProductFieldSetMapper implements FieldSetMapper<PartnerProduct> {
	@Override
	public PartnerProduct mapFieldSet(FieldSet fieldSet) throws BindException {
		PartnerProduct product = new PartnerProduct();
		product.setId(fieldSet.readString("PRODUCT_ID"));
		product.setTitle(fieldSet.readString("TITLE"));
		product.setDetails(fieldSet.readString("DETAILS"));
		product.setPrice(fieldSet.readBigDecimal("PRICE"));
		product.setReleaseDate(fieldSet.readDate("RELEASE_DATE"));

		return product;
	}
}


