package kr.experiments.springbatch.test.stop;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kr.experiments.springbatch.test.stop.EmptyWriter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 5:48
 */
@Component("productItemWriter")
public class EmptyWriter implements ItemWriter<String> {
	@Override
	public void write(List<? extends String> items) throws Exception {
		// nothing to do.
	}
}
