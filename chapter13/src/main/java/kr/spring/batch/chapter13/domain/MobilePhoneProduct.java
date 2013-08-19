package kr.spring.batch.chapter13.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * kr.spring.batch.chapter13.domain.MobilePhoneProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:27
 */
@Entity
@DiscriminatorValue("MobilePhone")
@Getter
@Setter
public class MobilePhoneProduct extends Product {

	private String manufacturer;

	private static final long serialVersionUID = -5886631781029655115L;
}
