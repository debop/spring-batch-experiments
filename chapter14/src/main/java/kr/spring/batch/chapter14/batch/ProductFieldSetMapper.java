package kr.spring.batch.chapter14.batch;

import kr.spring.batch.chapter14.domain.Product;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * CVS 같은 컬럼형 파일 정보로부터 엔티티를 빌드합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:31
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {
    public static final String FIELD_ID = "ID";
    public static final String FIELD_NAME = "NAME";
    public static final String FIELD_DESCRIPTION = "DESCRIPTION";
    public static final String FIELD_PRICE = "PRICE";

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();
        product.setId(fieldSet.readString(FIELD_ID));
        product.setName(fieldSet.readString(FIELD_NAME));
        product.setDescription(fieldSet.readString(FIELD_DESCRIPTION));
        product.setPrice(fieldSet.readBigDecimal(FIELD_PRICE));

        return product;
    }
}
