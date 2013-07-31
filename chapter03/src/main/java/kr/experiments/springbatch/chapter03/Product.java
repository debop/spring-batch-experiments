package kr.experiments.springbatch.chapter03;

import lombok.Data;

import java.io.Serializable;

/**
 * kr.experiments.springbatch.chapter03.Product
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오전 11:43
 */
@Data
public class Product implements Serializable {

    private String id;
    private String name;
    private String description;
    private float price;

    private static final long serialVersionUID = 3552827527136677964L;
}
