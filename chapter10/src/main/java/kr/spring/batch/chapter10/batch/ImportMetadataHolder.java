package kr.spring.batch.chapter10.batch;

/**
 * kr.spring.batch.chapter10.batch.ImportMetadataHolder
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 16. 오후 2:54
 */
public class ImportMetadataHolder {

    private ImportMetadata importMetadata;

    public ImportMetadata get() {
        return importMetadata;
    }

    public void set(ImportMetadata importMetadata) {
        this.importMetadata = importMetadata;
    }
}
