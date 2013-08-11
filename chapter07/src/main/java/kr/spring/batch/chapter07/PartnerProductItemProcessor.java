package kr.spring.batch.chapter07;

import lombok.Setter;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter07.PartnerProductItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:09
 */
public class PartnerProductItemProcessor implements ItemProcessor<PartnerProduct, Product> {

	@Setter PartnerProductMapper mapper;

	@Override
	public Product process(PartnerProduct partnerProduct) throws Exception {
		return mapper.map(partnerProduct);
	}
}
