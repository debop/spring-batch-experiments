package kr.spring.batch.chapter14.batch;

import kr.spring.batch.chapter14.repository.ProductRepository;
import lombok.Setter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * kr.spring.batch.chapter14.batch.ProductAvgReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 9:56
 */
public class ProductAvgReader implements ItemReader<BigDecimal> {

	@Setter ProductRepository productRepository;

	private AtomicInteger count = new AtomicInteger(0);

	@Override
	public BigDecimal read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (count.getAndIncrement() == 0)
			return new BigDecimal(productRepository.getAverage());
		return null;
	}
}
