package kr.experiments.springbatch.chapter02.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * kr.experiments.springbatch.chapter01.domain.Product
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 2:47
 */
@Data
public class Product implements Serializable {

	private String id;
	private String name;
	private String description;
	private BigDecimal price;

	public Product() {}

	public Product(String id) {
		this.id = id;
	}

	private static final long serialVersionUID = -4402010052037941957L;
}
