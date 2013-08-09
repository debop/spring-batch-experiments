package kr.spring.batch.chapter08.skip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

/**
 * 건너뛰기 용 리스너
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 1:51
 */
@Slf4j
public class Slf4jSkipListener {

    @OnSkipInRead
    public void logWhenSkipInLoad(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffpe = (FlatFileParseException) t;
            log.error(ffpe.getInput(), t);
        }
    }
}
