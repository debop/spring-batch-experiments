package kr.spring.batch.chapter14.test.batch.unit.reader;

import kr.spring.batch.chapter14.batch.ProductFieldSetMapper;
import kr.spring.batch.chapter14.domain.Product;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;

import java.math.BigDecimal;

import static kr.spring.batch.chapter14.batch.ProductFieldSetMapper.*;
import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter14.test.batch.unit.reader.ProductFieldSetMapperTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 19. 오전 9:47
 */
public class ProductFieldSetMapperTest {

    @Test
    public void mapFieldMapClassic() throws Exception {
        DefaultFieldSet fieldSet = new DefaultFieldSet(
            new String[]{ "id", "name", "desc", "100.25" },
            new String[]{ FIELD_ID, FIELD_NAME, FIELD_DESCRIPTION, FIELD_PRICE }
        );

        ProductFieldSetMapper mapper = new ProductFieldSetMapper();
        Product p = mapper.mapFieldSet(fieldSet);

        Assertions.assertThat(p.getId()).isEqualTo("id");
        Assertions.assertThat(p.getName()).isEqualTo("name");
        Assertions.assertThat(p.getDescription()).isEqualTo("desc");
        Assertions.assertThat(p.getPrice()).isEqualTo(new BigDecimal("100.25"));
    }

    @Test
    public void mapFieldSetMock() throws Exception {
        FieldSet fieldSet = mock(FieldSet.class);
        ProductFieldSetMapper mapper = new ProductFieldSetMapper();
        mapper.mapFieldSet(fieldSet);

        verify(fieldSet, times(1)).readString(FIELD_ID);
        verify(fieldSet, times(1)).readString(FIELD_NAME);
        verify(fieldSet, times(1)).readString(FIELD_DESCRIPTION);
        verify(fieldSet, times(1)).readBigDecimal(FIELD_PRICE);
        verifyNoMoreInteractions(fieldSet);
    }
}
