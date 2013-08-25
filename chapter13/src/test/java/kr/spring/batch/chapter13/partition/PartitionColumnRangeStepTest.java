package kr.spring.batch.chapter13.partition;

import kr.spring.batch.chapter13.domain.ProductForColumnRange;
import kr.spring.batch.chapter13.repository.ProductForColumnRangeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * kr.spring.batch.chapter13.partition.PartitionColumnRangeStepTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 5:08
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PartitionColumnRangeStepConfiguration.class })
public class PartitionColumnRangeStepTest {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	@Qualifier("partitionImportProductsJob")
	private Job partitionImportProductsJob;

	@Autowired
	ProductForColumnRangeRepository productForColumnRangeRepository;

	@Before
	public void initializeDatabase() throws Exception {
		int count = 55;
		for (int i = 0; i < 55; i++) {
			ProductForColumnRange p = new ProductForColumnRange();
			p.setName("Product " + i);
			p.setPrice(124.60f);

			productForColumnRangeRepository.save(p);
		}
		productForColumnRangeRepository.flush();
	}

	@Test
	public void multiThreadedStep() throws Exception {
		JobExecution partitionImportProductsJobExec =
				jobLauncher.run(partitionImportProductsJob,
				                new JobParameters());
	}
}
