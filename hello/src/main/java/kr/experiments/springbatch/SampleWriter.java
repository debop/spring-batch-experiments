package kr.experiments.springbatch;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SampleWriter implements ItemWriter<SampleItem> {
    @Override
    public void write(List<? extends SampleItem> items) throws Exception {
        for (SampleItem item : items) {
            System.out.println(item.getName());
        }
    }
}
