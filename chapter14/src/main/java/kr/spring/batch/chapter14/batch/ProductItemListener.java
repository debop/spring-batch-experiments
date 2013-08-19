package kr.spring.batch.chapter14.batch;

import kr.spring.batch.chapter14.domain.Product;
import lombok.Setter;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.item.file.FlatFileItemWriter;

import java.util.Arrays;

/**
 * Item Processing 한 결과가 실패시에는 원본 값을 예외용 Writer에 씁니다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:31
 */
public class ProductItemListener extends ItemListenerSupport<Product, Product> {

    @Setter
    private FlatFileItemWriter<Product> excludeWriter;


    @Override
    public void afterProcess(Product item, Product result) {
        if (result == null) {
            try {
                excludeWriter.write(Arrays.asList(item));
            } catch (Exception ignored) {}
        }
    }
}
