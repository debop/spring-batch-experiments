package kr.spring.batch.chapter05.file;

import kr.spring.batch.chapter05.Product;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * kr.spring.batch.chapter05.file.ProductFieldSetMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:42
 */
public class ProductFieldSetMapper implements FieldSetMapper<Product> {
    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();

        product.setId(fieldSet.readString("id"));
        product.setName(fieldSet.readString("name"));
        product.setDescription(fieldSet.readString("description"));
        product.setPrice(fieldSet.readFloat("price"));

        return product;
    }
}
