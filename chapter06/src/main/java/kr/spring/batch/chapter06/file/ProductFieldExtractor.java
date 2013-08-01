package kr.spring.batch.chapter06.file;

import kr.spring.batch.chapter06.Product;
import org.springframework.batch.item.file.transform.FieldExtractor;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter06.file.ProductFieldExtractor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 11:01
 */
public class ProductFieldExtractor implements FieldExtractor<Product> {
    @Override
    public Object[] extract(Product item) {
        return new Object[] {
                "BEGIN",
                item.getId(),
                item.getPrice(),
                item.getPrice().multiply(new BigDecimal("0.15")),
                item.getName(),
                "END"
        };
    }
}
