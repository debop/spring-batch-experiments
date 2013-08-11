package kr.spring.batch.chapter05.service;

import kr.spring.batch.chapter05.Product;

import java.util.List;

/**
 * kr.spring.batch.chapter05.service.ProductService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:52
 */
public interface ProductService {

	List<Product> getProducts();
}
