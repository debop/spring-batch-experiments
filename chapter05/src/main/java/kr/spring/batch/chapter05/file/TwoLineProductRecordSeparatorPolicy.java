package kr.spring.batch.chapter05.file;

import org.springframework.batch.item.file.separator.RecordSeparatorPolicy;

/**
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 1. 오후 1:48
 */
public class TwoLineProductRecordSeparatorPolicy implements RecordSeparatorPolicy {

    @Override
    public boolean isEndOfRecord(String record) {
        return countComma(record) == 3;
    }

    @Override
    public String preProcess(String record) {
        return record;
    }

    @Override
    public String postProcess(String record) {
        return record;
    }

    private static int countComma(final String s) {
        int index = -1;
        int count = 0;

        while ((index = s.indexOf(",", index + 1)) != -1) {
            count++;
        }
        return count;
    }
}
