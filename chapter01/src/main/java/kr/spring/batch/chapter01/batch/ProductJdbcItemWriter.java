package kr.spring.batch.chapter01.batch;

import kr.spring.batch.chapter01.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * 정보를 DB에 저장합니다.
 * TODO: Spring Data Jpa 를 이용하도록 변경해보자.
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 2:50
 */
@Slf4j
public class ProductJdbcItemWriter implements ItemWriter<Product> {

    private static final String INSERT_PRODUCT = "insert into product (id, name, description, price) values(?,?,?,?)";
    private static final String UPDATE_PRODUCT = "update product set name=?, description=?, price=? where id=?";

    private JdbcTemplate jdbcTemplate;

    public ProductJdbcItemWriter() {}

    public ProductJdbcItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /** 입력할 정보를 얻어 DB에 저장합니다. */
    @Override
    public void write(List<? extends Product> items) throws Exception {
        for (Product item : items) {
            ProductJdbcItemWriter.log.debug("UPSERT Product=[{}]", item);

            int updated = jdbcTemplate.update(UPDATE_PRODUCT,
                                              item.getName(),
                                              item.getDescription(),
                                              item.getPrice(),
                                              item.getId());
            if (updated == 0) {
                jdbcTemplate.update(INSERT_PRODUCT,
                                    item.getId(),
                                    item.getName(),
                                    item.getDescription(),
                                    item.getPrice());
            }
        }
    }
}
