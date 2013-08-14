package kr.spring.batch.chapter08.restart;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.comparator.NameFileComparator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.springframework.batch.item.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * kr.spring.batch.chapter08.restart.FilesInDirectoryItemReader
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 14. 오전 9:51
 */
@Slf4j
public class FilesInDirectoryItemReader implements ItemReader<File>, ItemStream {

    private File[] files;

    private int currentCount;

    private String key = "file.in.directory.count";

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        currentCount = executionContext.getInt(key, 0);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putInt(key, currentCount);
    }

    @Override
    public void close() throws ItemStreamException {
        // Nothing to do.
    }

    @Override
    public File read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        int index = currentCount++;
        return (index == files.length) ? null : files[index];
    }

    public void setDirectory(String directory) {
        this.files = new File(directory).listFiles((FileFilter) FileFilterUtils.fileFileFilter());
        Arrays.sort(files, new NameFileComparator());
    }
}
