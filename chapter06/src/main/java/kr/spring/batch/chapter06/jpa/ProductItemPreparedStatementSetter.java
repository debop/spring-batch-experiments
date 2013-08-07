package kr.spring.batch.chapter06.jpa;

import kr.spring.batch.chapter06.Product;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * kr.spring.batch.chapter06.jpa.ProductItemPreparedStatementSetter
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오후 2:49
 */
public class ProductItemPreparedStatementSetter implements ItemPreparedStatementSetter<Product> {
    @Override
    public void setValues(Product item, PreparedStatement ps) throws SQLException {
        ps.setString(1, item.getId());
        ps.setString(2, item.getName());
        ps.setBigDecimal(3, item.getPrice());
    }
}
