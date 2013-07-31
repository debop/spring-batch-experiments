package kr.experiments.springbatch.chapter03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

/**
 * kr.experiments.springbatch.chapter03.ProductFieldSetMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 11:53
 */
@Slf4j
@Component
public class ProductFieldSetMapper implements FieldSetMapper<Product> {

    // BUG: ModelMapper 로는 안된다.
    // private static final ModelMapper mapper = new ModelMapper();

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        // Product product = mapper.map(fieldSet, Product.class);

        Product product = new Product();
        product.setId(fieldSet.readString("id"));
        product.setName(fieldSet.readString("name"));
        product.setDescription(fieldSet.readString("description"));
        product.setPrice(fieldSet.readFloat("price"));

        log.info("Mapped Product=[{}]", product);

        return product;
    }
}
