package kr.spring.batch.chapter09.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

/**
 * kr.spring.batch.chapter09.domain.OrderItem
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:43
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class OrderItem implements Serializable {

	public OrderItem() {}

	public OrderItem(String productId, Integer quantity) {
		this.productId = productId;
		this.quantity = quantity;
	}

	@Id
	private Long id;

	@ManyToOne
	private Order order;

	private String productId;

	private Integer quantity;


	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof OrderItem) && (hashCode() == obj.hashCode());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(productId);
	}

	private static final long serialVersionUID = 3079230828084336402L;
}
