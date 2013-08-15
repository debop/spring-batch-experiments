package kr.spring.batch.chapter09.test.jta;

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

import javax.sql.DataSource;

/**
 * kr.spring.batch.chapter09.test.jta.JtaTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 11:07
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JtaConfiguration.class })
public class JtaTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Before
	public void setUp() throws Exception {
		jdbcTemplate.update("delete from product");
		jdbcTemplate.update(
				"insert into product (id,name,description,price) values(?,?,?,?)",
				"PR....214", "Nokia 2610 Phone", "", 102.23
		);
	}

	@Test
	public void batchTablesAndApplicationTablesOnDifferentDb() throws Exception {
		int initial = getProductCount();

		JobParameters params =
				new JobParametersBuilder()
						.addString("inputResource", "classpath:kr/spring/batch/chapter09/jta/products.zip")
						.addString("targetDirectory", ".target/importproductsbatch/")
						.addString("targetFile", "products.txt")
						.addLong("timestamp", System.currentTimeMillis())
						.toJobParameters();
		jobLauncher.run(job, params);

		Assertions.assertThat(getProductCount()).isEqualTo(initial + 7);
	}

	private int getProductCount() {
		return jdbcTemplate.queryForObject("select count(1) from product", Integer.class);
	}
}
