package kr.spring.batch.chapter10.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter10.test.JobWithListenersTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 7:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:kr/spring/batch/chapter10/batch-infrastructure.xml",
                        "classpath:kr/spring/batch/chapter10/job-with-listeners.xml" })
public class JobWithListenersTest extends AbstractJobTest {
}
