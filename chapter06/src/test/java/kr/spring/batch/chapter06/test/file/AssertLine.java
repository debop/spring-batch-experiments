package kr.spring.batch.chapter06.test.file;

import org.fest.assertions.Assertions;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * kr.spring.batch.chapter06.test.file.AssertLine
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 7. 오전 10:33
 */
public class AssertLine {

    public static void assertLineFileEquals(Resource resource, int lineNumber, String expectedLine) throws Exception {
        assertLineFileEquals(resource.getFile(), lineNumber, expectedLine);
    }

    public static void assertLineFileEquals(File file, int lineNumber, String expectedLine) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int lineNum = 1;
            String line = reader.readLine();
            while (line != null && lineNum < lineNumber) {
                lineNum++;
                line = reader.readLine();
            }
            Assertions.assertThat(lineNum).isEqualTo(lineNumber);
            Assertions.assertThat(line).isEqualTo(expectedLine);
        }
    }
}
