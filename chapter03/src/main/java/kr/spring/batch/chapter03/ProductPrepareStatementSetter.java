package kr.spring.batch.chapter03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * kr.spring.batch.chapter03.ProductPrepareStatementSetter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 12:59
 */
@Slf4j
public class ProductPrepareStatementSetter implements ItemPreparedStatementSetter<Product> {
    @Override
    public void setValues(Product product, PreparedStatement ps) throws SQLException {

        log.debug("Product 정보를 PreparedStatement에 설정합니다. product=[{}]", product);

        ps.setString(1, product.getId());
        ps.setString(2, product.getName());
        ps.setString(3, product.getDescription());
        ps.setFloat(4, product.getPrice());
    }
}
