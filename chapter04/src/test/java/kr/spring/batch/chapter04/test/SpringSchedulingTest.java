package kr.spring.batch.chapter04.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter04.test.SpringSchedulingTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 4:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/kr/spring/batch/chapter04/test/spring-scheduling-job.xml" })
public class SpringSchedulingTest {

    @Autowired
    CountDownLatch xmlCountDownLatch;

    @Autowired
    CountDownLatch annotationCountDownLatch;

    @Test
    public void xmlSpringScheduling() throws Exception {
        assertThat(xmlCountDownLatch.await(10, TimeUnit.SECONDS)).isTrue();
    }

    @Test
    public void annotationSpringScheduling() throws Exception {
        assertThat(annotationCountDownLatch.await(10, TimeUnit.SECONDS)).isTrue();
    }
}


