package kr.spring.batch.chapter08.retry;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.listener.RetryListenerSupport;

/**
 * kr.spring.batch.chapter08.retry.Slf4jRetryListener
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 9:31
 */
@Slf4j
public class Slf4jRetryListener extends RetryListenerSupport {
    @Override
    public <T> void onError(RetryContext context, RetryCallback<T> callback, Throwable throwable) {
        log.error("retried operation", throwable);
    }
}
