package kr.spring.batch.chapter08.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.context.RepeatContextSupport;
import org.springframework.batch.repeat.support.RepeatTemplate;

/**
 * kr.spring.batch.chapter08.test.RepeatTemplateTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:29
 */
@Slf4j
public class RepeatTemplateTest {

	@Test
	public void callbackInternalCount() {
		RepeatTemplate repeatTemplate = new RepeatTemplate();
		repeatTemplate.iterate(new RepeatCallback() {
			int count = 0;

			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				return ++count > 5 ? RepeatStatus.FINISHED : RepeatStatus.CONTINUABLE;
			}
		});
	}

	@Test
	public void callbackConetxtCount() {
		RepeatTemplate tpl = new RepeatTemplate();
		tpl.iterate(new RepeatCallback() {
			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				Integer count = (Integer) context.getAttribute("count");
				if (count == null)
					count = 0;
				count++;
				context.setAttribute("count", count);
				return count > 5 ? RepeatStatus.FINISHED : RepeatStatus.CONTINUABLE;
			}
		});
	}

	@Test
	public void completionStrategy() {
		RepeatTemplate tpl = new RepeatTemplate();
		tpl.setCompletionPolicy(new CompletionPolicy() {
			@Override
			public boolean isComplete(RepeatContext context, RepeatStatus result) {
				Integer count = (Integer) context.getAttribute("count");
				return count > 5;
			}

			@Override
			public boolean isComplete(RepeatContext context) {
				return isComplete(context, RepeatStatus.CONTINUABLE);
			}

			@Override
			public RepeatContext start(RepeatContext parent) {
				RepeatContextSupport ctx = new RepeatContextSupport(parent);
				ctx.setAttribute("count", 0);
				return ctx;
			}

			@Override
			public void update(RepeatContext context) {
				Integer count = (Integer) context.getAttribute("count");
				context.setAttribute("count", ++count);
			}
		});

		tpl.iterate(new RepeatCallback() {
			@Override
			public RepeatStatus doInIteration(RepeatContext context) throws Exception {
				return RepeatStatus.CONTINUABLE;
			}
		});
	}
}
