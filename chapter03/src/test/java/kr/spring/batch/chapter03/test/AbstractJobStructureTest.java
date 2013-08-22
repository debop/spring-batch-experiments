package kr.spring.batch.chapter03.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter03.test.AbstractJobStructureTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 1:05
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractJobStructureTest {

    @Autowired
    protected Job job;

    @Autowired
    protected JobLauncher jobLauncher;
}
