package kr.spring.batch.chapter07;

import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter07.PartnerIdItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:48
 */
public class PartnerIdItemProcessor implements ItemProcessor<Product, Product> {

    @Setter
    private PartnerIdMapper mapper;

    @Override
    public Product process(Product item) throws Exception {
        return mapper.map(item);
    }
}
