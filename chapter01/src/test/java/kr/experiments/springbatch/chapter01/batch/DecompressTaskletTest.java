package kr.experiments.springbatch.chapter01.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.experiments.springbatch.chapter01.batch.DecompressTaskletTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 4:16
 */
@Slf4j
public class DecompressTaskletTest {

    private static final String[] EXPECTED_CONTENT = new String[]{
        "PRODUCT_ID,NAME,DESCRIPTION,PRICE",
        "PR....210,BlackBerry 8100 Pearl,,124.60",
        "PR....211,Sony Ericsson W810i,,139.45",
        "PR....212,Samsung MM-A900M Ace,,97.80",
        "PR....213,Toshiba M285-E 14,,166.20",
        "PR....214,Nokia 2610 Phone,,145.50",
        "PR....215,CN Clogs Beach/Garden Clog,,190.70",
        "PR....216,AT&T 8525 PDA,,289.20",
        "PR....217,Canon Digital Rebel XT 8MP Digital SLR Camera,,13.70",
    };


    @Test
    public void execute() throws Exception {
        DecompressTasklet tasklet = new DecompressTasklet();

        tasklet.setInputResource(new ClassPathResource("/input/products.zip"));
        File outputDir = new File("./target/decompresstasklet");
        if (outputDir.exists()) {
            FileUtils.deleteDirectory(outputDir);
        }
        tasklet.setTargetDirectory(outputDir.getAbsolutePath());
        tasklet.setTargetFile("products.txt");

        tasklet.execute(null, null);

        File output = new File(outputDir, "products.txt");
        assertThat(output.exists()).isTrue();
        assertThat(FileUtils.readLines(output).toArray()).isEqualTo(EXPECTED_CONTENT);
    }

    @Test(expected=IllegalStateException.class)
    public void corruptedArchive() throws Exception {
        DecompressTasklet tasklet = new DecompressTasklet();

        tasklet.setInputResource(new ClassPathResource("/input/products_corrupted.zip"));
        File outputDir = new File("./target/decompresstasklet");
        if (outputDir.exists()) {
            FileUtils.deleteDirectory(outputDir);
        }
        tasklet.setTargetDirectory(outputDir.getAbsolutePath());
        tasklet.setTargetFile("products.txt");

        tasklet.execute(null, null);
    }
}
