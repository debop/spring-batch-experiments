package kr.spring.batch.chapter05.jpa.repository;

import kr.spring.batch.chapter05.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter05.jpa.repository.ProductRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 5:34
 */
public interface ProductRepository extends JpaRepository<Product, String> {
}
