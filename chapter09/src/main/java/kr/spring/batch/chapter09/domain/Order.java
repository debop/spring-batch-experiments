package kr.spring.batch.chapter09.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * kr.spring.batch.chapter09.domain.Order
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 15. 오전 9:39
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Order implements Serializable {

	@Id
	private String orderId;

	private Boolean shipped;

	@OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<OrderItem> items = new ArrayList<OrderItem>();


	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof Order) && (hashCode() == obj.hashCode());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(orderId);
	}

	private static final long serialVersionUID = -7328951984688248444L;
}
