package kr.spring.batch.chapter05.test.file;

import kr.spring.batch.chapter05.test.AbstractJobStructureTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.test.context.ContextConfiguration;

/**
 * kr.spring.batch.chapter05.test.file.JobStructureDelimitedTwoLinesFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 2:05
 */
@Slf4j
@ContextConfiguration(classes = { JobStructureDelimitedTwoLinesFlatFileConfig.class })
public class JobStructureDelimitedTwoLinesFlatFileTest extends AbstractJobStructureTest {

    @Test
    public void existingServiceJob() throws Exception {
        jobLauncher.run(job, new JobParameters());

        checkProducts(writer.getProducts(), new String[]{ "PR....210",
                                                          "PR....211",
                                                          "PR....212",
                                                          "PR....213",
                                                          "PR....214",
                                                          "PR....215",
                                                          "PR....216",
                                                          "PR....217" });
    }
}
