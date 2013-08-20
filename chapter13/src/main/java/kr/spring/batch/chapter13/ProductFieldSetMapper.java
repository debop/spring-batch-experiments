package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.BookProduct;
import kr.spring.batch.chapter13.domain.MobilePhoneProduct;
import kr.spring.batch.chapter13.domain.Product;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * kr.spring.batch.chapter13.ProductFieldSetMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:18
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        String id = fieldSet.readString("id");
        Product product = null;
        if (id.startsWith("PRB")) {
            product = new BookProduct();
        } else if (id.startsWith("PRM")) {
            product = new MobilePhoneProduct();
        } else {
            product = new Product();
        }

        product.setId(id);
        product.setName(fieldSet.readString("name"));
        product.setDescription(fieldSet.readString("description"));
        product.setPrice(fieldSet.readFloat("price"));

        return product;
    }
}
