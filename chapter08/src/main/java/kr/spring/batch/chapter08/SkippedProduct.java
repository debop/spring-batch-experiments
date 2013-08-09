package kr.spring.batch.chapter08;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * kr.spring.batch.chapter08.SkippedProduct
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 1:57
 */
@Entity
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class SkippedProduct implements Serializable {

    @Id
    @GeneratedValue
    public Long id;

    public Integer lineNumber;

    public String input;

    @Override
    public String toString() {
        return "SkippedProduct# lineNumber=" + lineNumber + ", input=" + input;
    }

    private static final long serialVersionUID = 2462868704626608656L;
}
