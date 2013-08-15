package kr.spring.batch.chapter09.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
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
@Table(name = "Orders")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Order implements Serializable {

	public Order() {}

	public Order(String orderId, boolean shipped) {
		this.orderId = orderId;
		this.shipped = shipped;
	}

	@Id
	@GeneratedValue
	private Long id;

	private String orderId;

	private Boolean shipped;

	@OneToMany(mappedBy = "order", cascade = { CascadeType.ALL }, orphanRemoval = true)
	@LazyCollection(LazyCollectionOption.EXTRA)
	private List<OrderItem> items = new ArrayList<OrderItem>();


	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof Order) && (hashCode() == obj.hashCode());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	private static final long serialVersionUID = -7328951984688248444L;
}
