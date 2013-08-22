package kr.spring.batch.chapter01.test.batch;

import kr.spring.batch.chapter01.config.ImportProductJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter01.test.batch.ImportProductsByJavaConfigTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 7:44
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ImportProductJobConfiguration.class })
public class ImportProductsByJavaConfigTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importProductsJob;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() throws Exception {
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update(
            "insert into product(id, name, description, price) values(?,?,?,?)",
            "PR....214", "Nokia 2610 Phone", "", 102.23
        );
    }

    @Test
    public void importProducts() throws Exception {
        int initial = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);

        JobParameters jobParameters =
            new JobParametersBuilder()
                .addString("inputResource", "/input/products.zip")
                .addString("targetDirectory", "./target/importProductsBatchByJavaConfig/")
                .addString("targetFile", "products.txt")
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(importProductsJob, jobParameters);

        int nbOfNewProducts = 7;
        int totalNumber = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);
        Assertions.assertThat(totalNumber - initial).isEqualTo(nbOfNewProducts);
    }
}
