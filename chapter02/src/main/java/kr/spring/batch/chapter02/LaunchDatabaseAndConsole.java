package kr.spring.batch.chapter02;

import kr.spring.batch.chapter02.config.RootDatabaseConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * kr.spring.batch.chapter02.LaunchDatabaseAndConsole
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 7. 30. 오후 11:35
 */
@Slf4j
public class LaunchDatabaseAndConsole {

    public static void main(String[] args) throws Exception {
        new AnnotationConfigApplicationContext(RootDatabaseConfiguration.class);
    }
}
