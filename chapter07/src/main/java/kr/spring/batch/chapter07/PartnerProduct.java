package kr.spring.batch.chapter07;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * kr.spring.batch.chapter07.PartnerProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 6:05
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class PartnerProduct implements Serializable {

    @Id
    private String id;

    private String title;

    private String details;

    private BigDecimal price;

    private Date releaseDate;

    private static final long serialVersionUID = 201275399428244388L;
}
