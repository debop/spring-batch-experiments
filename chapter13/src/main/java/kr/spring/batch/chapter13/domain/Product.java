package kr.spring.batch.chapter13.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

/**
 * kr.spring.batch.chapter13.domain.Product
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:19
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "productType")
@DiscriminatorValue("Product")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Product implements Serializable {

    public Product() {}

    public Product(String id) {
        this.id = id;
    }

    @Id
    private String id;

    private String name;

    private String description;

    private float price;

    private boolean processed;

    @Override
    public String toString() {
        return "Product# id=[" + id + "], name=[" + name + "]";
    }

    private static final long serialVersionUID = 7647304969032217912L;
}
