package kr.spring.batch.chapter13.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * kr.spring.batch.chapter13.domain.BookProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오후 11:26
 */
@Entity
@DiscriminatorValue("MobilePhone")
@Getter
@Setter
public class BookProduct extends Product {

	private String publisher;

	private static final long serialVersionUID = -6363476647920249030L;
}
