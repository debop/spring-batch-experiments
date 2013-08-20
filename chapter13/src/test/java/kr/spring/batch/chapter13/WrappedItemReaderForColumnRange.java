package kr.spring.batch.chapter13;

import kr.spring.batch.chapter13.domain.ProductForColumnRange;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.item.*;

/**
 * kr.spring.batch.chapter13.WrappedItemReaderForColumnRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 2:32
 */
public class WrappedItemReaderForColumnRange implements ItemReader<ProductForColumnRange>, ItemStream {

    @Getter
    @Setter
    private ItemReader<ProductForColumnRange> delegate;

    @Override
    public ProductForColumnRange read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ProductForColumnRange product = delegate.read();
        ThreadUtils.writeThreadExecutionMessage("read", product);
        return product;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (this.delegate instanceof ItemStream) {
            ((ItemStream) this.delegate).open(executionContext);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        if (this.delegate instanceof ItemStream) {
            ((ItemStream) this.delegate).update(executionContext);
        }
    }

    @Override
    public void close() throws ItemStreamException {
        if (this.delegate instanceof ItemStream) {
            ((ItemStream) this.delegate).close();
        }
    }
}
