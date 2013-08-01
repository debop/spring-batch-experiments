package kr.spring.batch.chapter05.file;

import kr.spring.batch.chapter05.Product;
import lombok.Setter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.JsonLineMapper;

import java.util.Map;

/**
 * JSON 포맷 문자열을 읽어, Map으로 변환한 후 Product를 빌드한다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:44
 */
public class WrappedJsonLineMapper implements LineMapper<Product> {

    @Setter private JsonLineMapper delegate;

    @Override
    public Product mapLine(String line, int lineNumber) throws Exception {

        // JSON 포맷 문자열을 읽어, Map 으로 변환한다.
        Map<String, Object> productAsMap = delegate.mapLine(line, lineNumber);

        Product product = new Product();
        product.setId((String) productAsMap.get("id"));
        product.setName((String) productAsMap.get("name"));
        product.setDescription((String) productAsMap.get("description"));
        product.setPrice(Float.parseFloat(productAsMap.get("price").toString()));

        return product;
    }
}
