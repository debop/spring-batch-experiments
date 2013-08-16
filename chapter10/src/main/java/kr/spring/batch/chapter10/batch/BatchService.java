package kr.spring.batch.chapter10.batch;

import kr.spring.batch.chapter10.exception.IntegrityViolationException;
import org.springframework.batch.core.JobExecution;

/**
 * kr.spring.batch.chapter10.batch.BatchService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 2:46
 */
public interface BatchService {

    void download(String targetFile);

    void decompress(String inputFile, String outputDirectory);

    void verify(String outputDirectory) throws IntegrityViolationException;

    void clean(String outputDirectory);

    /** Track the import in the database. */
    void track(String importId);

    ImportMetadata extractMetadata(String outputDirectory);

    boolean exists(String file);

    void generateReport(JobExecution jobExecution);
}
