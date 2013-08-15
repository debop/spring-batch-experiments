package kr.spring.batch.chapter09.test.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * kr.spring.batch.chapter08.test.beans.DummyItemWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:26
 */
@Slf4j
public class DummyItemWriter implements ItemWriter<String> {

	private BusinessService service;

	public DummyItemWriter(BusinessService service) {
		this.service = service;
	}

	@Override
	public void write(List<? extends String> items) throws Exception {

		for (String item : items) {
			log.debug("writing [{}]", item);
			service.writing(item);
			log.debug("item written [{}]", item);
		}
	}
}
