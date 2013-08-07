package kr.spring.batch.chapter06.file;

import kr.spring.batch.chapter06.Product;
import lombok.Setter;
import org.springframework.batch.item.file.transform.LineAggregator;

import java.util.Map;

/**
 * {@link Product} 정보를 추출한 {@link ProductFieldExtractor} 들을 묶어서 여러 개의 Line으로 취합합니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 11:11
 */
public class ProductLineAggregator implements LineAggregator<Product> {

    @Setter
    private Map<Class<LineAggregator<Product>>, LineAggregator<Object>> aggregators;

    @Override
    public String aggregate(Product product) {
        return this.aggregators.get(product.getClass()).aggregate(product);
    }
}
