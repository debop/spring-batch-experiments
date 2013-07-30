package kr.experiments.springbatch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class SampleItemFieldSetMapper implements FieldSetMapper<SampleItem> {
    @Override
    public SampleItem mapFieldSet(FieldSet fieldSet) throws BindException {
        SampleItem sampleItem = new SampleItem(fieldSet.readString(0));
        return sampleItem;
    }
}
