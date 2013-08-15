package kr.spring.batch.chapter09.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * kr.spring.batch.chapter09.domain.InventoryOrder
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 10:46
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class InventoryOrder implements Serializable {

	public InventoryOrder() {}

	public InventoryOrder(Order order, Date processingDate) {
		this.order = order;
		this.processingDate = processingDate;
	}

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Order order;

	@Temporal(TemporalType.TIMESTAMP)
	private Date processingDate;

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof InventoryOrder) && (hashCode() == obj.hashCode());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	private static final long serialVersionUID = 311757910742231517L;
}
