package kr.experiments.springbatch.chapter03;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * kr.experiments.springbatch.chapter03.ProductFieldSetMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 11:53
 */
@Slf4j
public class ProductFieldSetMapper implements FieldSetMapper<Product> {

    private static final ModelMapper mapper = new ModelMapper();

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = mapper.map(fieldSet, Product.class);
        log.info("Mapped Product=[{}]", product);
        return product;
    }
}
