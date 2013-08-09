package kr.spring.batch.chapter08.skip;

import kr.spring.batch.chapter08.SkippedProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.item.file.FlatFileParseException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * kr.spring.batch.chapter08.skip.DatabaseSkipListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 1:54
 */
@Slf4j
public class DatabaseSkipListener extends SkipListenerSupport {

    @PersistenceContext
    EntityManager em;

    @Override
    public void onSkipInRead(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            SkippedProduct product = new SkippedProduct();
            product.setInput(ffpe.getInput());
            product.setLineNumber(ffpe.getLineNumber());
            em.persist(product);
            em.flush();
        }
    }
}
