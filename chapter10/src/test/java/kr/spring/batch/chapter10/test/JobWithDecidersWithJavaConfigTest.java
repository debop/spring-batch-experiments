package kr.spring.batch.chapter10.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter10.test.JobWithDecidersTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 4:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JobWithDecidersConfiguration.class })
public class JobWithDecidersWithJavaConfigTest extends AbstractJobTest {
}
