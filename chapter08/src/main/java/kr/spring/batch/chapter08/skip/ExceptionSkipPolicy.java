package kr.spring.batch.chapter08.skip;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * 건너뛰기 정책 (특정 Exception 및 자식 Exception이라면 Skip 합니다.)
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:02
 */
public class ExceptionSkipPolicy implements SkipPolicy {

	private Class<? extends Exception> exceptionClassToSkip;

	public ExceptionSkipPolicy(Class<? extends Exception> exceptionClassToSkip) {
		this.exceptionClassToSkip = exceptionClassToSkip;
	}

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		return exceptionClassToSkip.isAssignableFrom(t.getClass());
	}
}
