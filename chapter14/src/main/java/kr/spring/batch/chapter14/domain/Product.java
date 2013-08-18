package kr.spring.batch.chapter14.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * kr.spring.batch.chapter14.domain.Product
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:30
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product implements Serializable {

	@Id
	private String id;

	private String name;

	private String description;

	private BigDecimal price;

	@Override
	public String toString() {
		return getId() + "|" + getName();
	}

	private static final long serialVersionUID = -413446600801113135L;
}
