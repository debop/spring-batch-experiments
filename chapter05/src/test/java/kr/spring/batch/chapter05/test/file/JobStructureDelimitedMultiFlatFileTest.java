package kr.spring.batch.chapter05.test.file;

import kr.spring.batch.chapter05.test.AbstractJobStructureTest;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.test.context.ContextConfiguration;

/**
 * kr.spring.batch.chapter05.test.file.JobStructureDelimitedMultiFlatFileTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 2:05
 */
@ContextConfiguration(classes = { JobStructureDelimitedMultiFlatFileConfig.class })
public class JobStructureDelimitedMultiFlatFileTest extends AbstractJobStructureTest {

    @Test
    public void existingServiceJob() throws Exception {
        jobLauncher.run(job, new JobParameters());

        checkProducts(writer.getProducts(), new String[]{ "PRM....210",
                                                          "PRM....211",
                                                          "PRB....734",
                                                          "PRM....212",
                                                          "PRB....735",
                                                          "PRM....213",
                                                          "PRB....736",
                                                          "PRM....214" });
    }
}
