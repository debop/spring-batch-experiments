package kr.spring.batch.chapter04.test;

import org.fest.assertions.Assertions;
import org.junit.Test;
import org.quartz.Scheduler;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * kr.spring.batch.chapter04.test.QuartzSchedulingTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 22. 오후 4:28
 */
public class QuartzSchedulingTest {

	@Test
	public void springScheduling() throws Exception {
		// doesn't seem to work with test context framework
		// quartz complains about not finding a database table,
		// perhaps the quartz thread is still working whereas the in-memory datasource is closed?

//        ConfigurableApplicationContext ctx =
//            new ClassPathXmlApplicationContext("classpath:/kr/spring/batch/chapter04/test/quartz-scheduling-job.xml");
		ConfigurableApplicationContext ctx =
				new AnnotationConfigApplicationContext(QuartzSchedulingConfiguration.class);

		Scheduler scheduler = ctx.getBean(Scheduler.class);
		scheduler.start();

		CountDownLatch countDownLatch = ctx.getBean(CountDownLatch.class);
		Assertions.assertThat(countDownLatch.await(10, TimeUnit.SECONDS)).isTrue();

		ctx.close();
	}
}
