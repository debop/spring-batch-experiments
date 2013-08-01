package kr.spring.batch.chapter05.test.jpa;

import com.google.common.collect.Lists;
import kr.spring.batch.chapter05.Product;
import kr.spring.batch.chapter05.jpa.repository.ProductRepository;
import kr.spring.batch.chapter05.test.AbstractJobStructureTest;
import lombok.extern.slf4j.Slf4j;
import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * kr.spring.batch.chapter05.test.jpa.JobStructureJpaCursorTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 2:03
 */
@Slf4j
@ContextConfiguration(classes = { JobStructureJpaCursorConfig.class })
public class JobStructureJpaCursorTest extends AbstractJobStructureTest {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getProducts() {
        List<Product> products = Lists.newArrayList();

        products.add(new Product("PR....210", "BlackBerry 8100 Pearl", "", 124.60f));
        products.add(new Product("PR....211", "Sony Ericsson W810i", "", 139.45f));
        products.add(new Product("PR....212", "Samsung MM-A900M Ace", "", 97.80f));
        products.add(new Product("PR....213", "Toshiba M285-E 14", "", 166.20f));
        products.add(new Product("PR....214", "Nokia 2610 Phone", "", 145.50f));
        products.add(new Product("PR....215", "CN Clogs Beach/Garden Clog", "", 190.70f));
        products.add(new Product("PR....216", "AT&T 8525 PDA", "", 289.20f));
        products.add(new Product("PR....217", "Canon Digital Rebel XT 8MP Digital SLR Camera", "", 13.70f));

        return products;
    }

    @Before
    public void insertProduct() {
        productRepository.deleteAll();
        productRepository.save(getProducts());
        productRepository.flush();
    }

    @Test
    public void loadProduct() {
        List<Product> products = productRepository.findAll();
        Assertions.assertThat(products).hasSize(8);
    }

    @Test
    public void hibernateCursorJob() throws Exception {
        jobLauncher.run(job, new JobParameters());
        checkProducts(writer.getProducts(), new String[]{ "PR....210", "PR....211", "PR....212",
                                                          "PR....213", "PR....214", "PR....215", "PR....216", "PR....217" });
    }

}
