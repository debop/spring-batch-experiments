package kr.experiments.springbatch.test.stop;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

/**
 * kr.experiments.springbatch.test.stop.InfiniteReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 5:48
 */
@Component("productItemReader")
public class InfiniteReader implements ItemReader<String> {
    private int count = 0;

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return ++count + "";
    }
}
