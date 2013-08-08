package kr.spring.batch.chapter07;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * kr.spring.batch.chapter07.PartnerMapping
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:54
 */
@Entity
@Table(name = "partner_mapping")
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class PartnerMapping implements Serializable {

    public PartnerMapping() {}

    public PartnerMapping(String partnerId, String partnerProductId, String storedProductId) {
        this.partnerId = partnerId;
        this.productId = partnerProductId;
        this.id = storedProductId;
    }

    @Id
    @Column(name = "stored_product_id")
    private String id;

    @Column(name = "partner_id")
    private String partnerId;

    @Column(name = "partner_productId")
    private String productId;

    private static final long serialVersionUID = 3743425567027351965L;
}
