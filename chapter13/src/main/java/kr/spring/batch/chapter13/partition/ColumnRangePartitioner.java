package kr.spring.batch.chapter13.partition;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

/**
 * kr.spring.batch.chapter13.partition.ColumnRangePartitioner
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 20. 오전 11:18
 */
@Slf4j
@Getter
@Setter
@Component
public class ColumnRangePartitioner implements Partitioner {

    @PersistenceContext
    private EntityManager em;

    private String entityName;
    private String propertyName;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        final String minQuery = "select min(" + propertyName + ") from " + entityName;
        final String maxQuery = "select max(" + propertyName + ") from " + entityName;

        log.info("minQuery=[{}]", minQuery);
        log.info("maxQuery=[{}]", maxQuery);

        Long min = (Long) em.createQuery(minQuery).getSingleResult();
        Long max = (Long) em.createQuery(maxQuery).getSingleResult();
        if (min == null || max == null)
            new HashMap<String, ExecutionContext>();

        long targetSize = (max - min) / gridSize + 1;

        log.debug("range min=[{}], max=[{}], targetSize=[{}]", min, max, targetSize);

        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();
        int number = 0;
        long start = min;
        long end = start + targetSize - 1;

        while (start <= max) {
            ExecutionContext value = new ExecutionContext();
            result.put("partition" + number, value);

            if (end >= max) end = max;

            value.putLong("minValue", start);
            value.putLong("maxValue", end);

            start += targetSize;
            end += targetSize;
            number++;
        }
        return result;
    }
}
