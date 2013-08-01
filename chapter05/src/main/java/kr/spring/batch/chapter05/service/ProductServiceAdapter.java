package kr.spring.batch.chapter05.service;

import kr.spring.batch.chapter05.Product;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * NOTE: InitializingBean 을 상속받는 Bean 은 java config에서 afterPropertiesSet을 호출해줘야 하기 때문에 @Component로 지정하면 안된다.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:57
 */
public class ProductServiceAdapter implements InitializingBean {

    @Setter private ProductService productService;

    private List<Product> products;

    @Override
    public void afterPropertiesSet() throws Exception {
        assert productService != null;
        this.products = productService.getProducts();
    }

    public Product getProduct() {
        return (products.size() > 0) ? products.remove(0) : null;
    }
}
