package kr.spring.batch.chapter09.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * kr.spring.batch.chapter09.domain.Inventory
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:29
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Inventory implements Serializable {

	public Inventory() {}

	public Inventory(String productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	@Id
	@GeneratedValue
	private Long id;

	private Integer quantity;

	private String productId;

	public void decreaseQuantity(Integer quantityDelta) {
		quantity = quantity - quantityDelta;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof Inventory) && (hashCode() == obj.hashCode());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	private static final long serialVersionUID = -7376671735411767607L;
}
