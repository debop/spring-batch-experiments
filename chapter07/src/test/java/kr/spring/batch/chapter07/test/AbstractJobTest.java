package kr.spring.batch.chapter07.test;

import com.google.common.collect.Lists;
import kr.spring.batch.chapter07.PartnerMapping;
import kr.spring.batch.chapter07.Product;
import kr.spring.batch.chapter07.jpa.PartnerMappingRepository;
import kr.spring.batch.chapter07.jpa.ProductRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * kr.spring.batch.chapter07.test.AbstractJobTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 4:28
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractJobTest {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected PartnerMappingRepository partnerMappingRepository;

    public List<Product> getProducts() {
        List<Product> products = Lists.newArrayList();

        products.add(new Product("PR....214", "Nokia 2610 Phone", "A cell phone", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....215", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....216", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....217", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....218", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....219", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....220", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....221", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        products.add(new Product("PR....222", "Nokia 2610 Phone", "", BigDecimal.valueOf(102.23)));
        return products;
    }


    public List<PartnerMapping> getPartnerMappings() {
        List<PartnerMapping> partnerMappings = new ArrayList<PartnerMapping>();

        partnerMappings.add(new PartnerMapping("PARTNER1", "211", "PR....211"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "212", "PR....212"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "213", "PR....213"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "214", "PR....214"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "215", "PR....215"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "216", "PR....216"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "217", "PR....217"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "218", "PR....218"));
        partnerMappings.add(new PartnerMapping("PARTNER1", "219", "PR....219"));

        return partnerMappings;
    }


    @Before
    public void setUp() throws Exception {

        productRepository.deleteAll();
        partnerMappingRepository.deleteAll();
        productRepository.flush();

        productRepository.save(getProducts());
        partnerMappingRepository.save(getPartnerMappings());
        partnerMappingRepository.flush();
    }
}
