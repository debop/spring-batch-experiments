package kr.spring.batch.chapter10.exception;

/**
 * kr.spring.batch.chapter10.exception.IntegrityViolationException
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 2:48
 */
public class IntegrityViolationException extends RuntimeException {

    public IntegrityViolationException() { }

    public IntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public IntegrityViolationException(String message) {
        super(message);
    }

    public IntegrityViolationException(Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = -5102567368429215847L;
}
