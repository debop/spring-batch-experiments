package kr.spring.batch.chapter07;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * kr.spring.batch.chapter07.Product
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 6:05
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product implements Serializable {

    public Product() {}

    public Product(String id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @Id
    private String id;

    private String name;

    private String description;

    @NotNull
    @Min(0)
    private BigDecimal price = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTimestamp = new Date();

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + "]";
    }

    private static final long serialVersionUID = -6654175076448295912L;
}
