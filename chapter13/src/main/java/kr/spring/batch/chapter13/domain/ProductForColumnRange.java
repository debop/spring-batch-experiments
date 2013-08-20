package kr.spring.batch.chapter13.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * kr.spring.batch.chapter13.domain.ProductForColumnRange
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:24
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ProductForColumnRange implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private float price;

    @Override
    public String toString() {
        return "ProductForColumnRange# id=[" + id + "], name=[" + name + "]";
    }

    private static final long serialVersionUID = -4686885077293578413L;
}
