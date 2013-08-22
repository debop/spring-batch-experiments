package kr.spring.batch.chapter04.test;

import org.junit.Test;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.batch.core.launch.support.SystemExiter;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter04.test.CommandLineJobRunnterTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 31. 오후 3:58
 */
public class CommandLineJobRunnterTest {

    private static final String JOB_CONTEXT = "/kr/spring/batch/chapter04/import-products-job-exit-code.xml";

    @Test
    public void run() throws Exception {

        final Queue<Integer> exitCode = new ArrayBlockingQueue<Integer>(1);
        CommandLineJobRunner.presetSystemExiter(new SystemExiter() {
            @Override
            public void exit(int status) {
                exitCode.add(status);
            }
        });

        CommandLineJobRunner.main(new String[]{
            JOB_CONTEXT,
            "importProductsJob"
        });
        assertThat(exitCode.poll().intValue()).isEqualTo(0);

        CommandLineJobRunner.main(new String[]{
            JOB_CONTEXT,
            "importProductsJob",
            "exit.status=COMPLETED"
        });
        assertThat(exitCode.poll().intValue()).isEqualTo(0);

        CommandLineJobRunner.main(new String[]{
            JOB_CONTEXT,
            "importProductsJob",
            "exit.status=FAILED"
        });
        assertThat(exitCode.poll().intValue()).isEqualTo(1);

        CommandLineJobRunner.main(new String[]{
            JOB_CONTEXT,
            "importProductsJob",
            "exit.status=COMPLETED WITH SKIPS"
        });
        assertThat(exitCode.poll().intValue()).isEqualTo(3);

        CommandLineJobRunner.main(new String[]{
            JOB_CONTEXT,
            "importProductsJob",
            "exit.status=ANYTHING"
        });
        assertThat(exitCode.poll().intValue()).isEqualTo(2);
    }
}
