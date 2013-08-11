package kr.spring.batch.chapter05.service;

import com.google.common.collect.Lists;
import kr.spring.batch.chapter05.Product;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * kr.spring.batch.chapter05.service.ProductServiceImpl
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:53
 */
@Component("productService")
public class ProductServiceImpl implements ProductService {


	@Override
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
}
