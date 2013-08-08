package kr.spring.batch.chapter07.test.process;

import kr.spring.batch.chapter07.test.AbstractJobTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter07.test.process.ItemProcessorConfigurationTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:25
 */
@Slf4j
@ContextConfiguration(classes = { ItemProcessorConfiguration.class })
public class ItemProcessorConfigurationTest extends AbstractJobTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    private String targetFile = "target/output.txt";

    @Test
    public void itemProcessConfiguration() throws Exception {
        assertThat(productRepository.count()).isEqualTo(9);

        JobExecution exec = jobLauncher.run(job,
                                            new JobParametersBuilder().addString("targetFile", targetFile)
                                                                      .toJobParameters());
        assertThat(exec.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        Resource resource = new FileSystemResource(targetFile);
        assertThat(FileUtils.readLines(resource.getFile()).size()).isEqualTo(5);
    }
}
