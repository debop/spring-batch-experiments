package kr.spring.batch.chapter13.partition;

import kr.spring.batch.chapter13.domain.ProductForColumnRange;
import kr.spring.batch.chapter13.repository.ProductForColumnRangeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * kr.spring.batch.chapter13.partition.ColumnRangePartitionerTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오후 5:31
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:kr/spring/batch/chapter13/partition/PartitionColumnRangeStepTest-context.xml" })
public class ColumnRangePartitionerTest {

    @Autowired
    ProductForColumnRangeRepository productForColumnRangeRepository;

    @PersistenceContext
    EntityManager em;

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
    public void getMinValue() {
        long min = (Long) em.createQuery("select min(id) from ProductForColumnRange").getSingleResult();
        log.debug("min = [{}]", min);
    }

    @Test
    public void getMaxValue() {
        long max = (Long) em.createQuery("select max(id) from ProductForColumnRange").getSingleResult();
        log.debug("max = [{}]", max);
    }
}
