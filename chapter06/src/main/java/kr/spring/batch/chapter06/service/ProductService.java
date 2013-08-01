package kr.spring.batch.chapter06.service;

import kr.spring.batch.chapter06.Product;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter06.service.ProductService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 10:22
 */
@Slf4j
public class ProductService {

    public void write(Product product) {
        log.info("기존 서비스 메소드를 사용합니다. writing product id=[{}]", product.getId());
    }

    public void write(String id, String name, String description, BigDecimal price) {
        log.info("기존 서비스 메소드를 사용합니다. product id=[{}], name=[{}]", id, name);
    }
}
