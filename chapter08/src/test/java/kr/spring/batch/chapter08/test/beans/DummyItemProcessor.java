package kr.spring.batch.chapter08.test.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * kr.spring.batch.chapter08.test.beans.DummyItemProcessor
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:24
 */
@Slf4j
public class DummyItemProcessor implements ItemProcessor<String, String> {

	private BusinessService service;

	public DummyItemProcessor(BusinessService service) {
		this.service = service;
	}

	@Override
	public String process(String item) throws Exception {
		log.debug("processing [{}]", item);
		service.processing(item);
		log.debug("after processing [{}]", item);
		return item;
	}
}
