package kr.spring.batch.chapter08.test;

import lombok.extern.slf4j.Slf4j;
import org.fest.util.Files;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.net.URL;

import static org.fest.assertions.Assertions.assertThat;

/**
 * kr.spring.batch.chapter08.test.ResourceUtilsTest
 * <p/>
 * HINT: http://stackoverflow.com/questions/5637765/how-to-deal-with-relative-path-in-junits-between-maven-and-intellij
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 10. 오후 12:22
 */
@Slf4j
public class ResourceUtilsTest {

	@Test
	public void classpathResource() throws Exception {
		String resourceLocation = "classpath:./skip/products_no_error.txt";
		assertThat(ResourceUtils.isUrl(resourceLocation)).isTrue();
		assertThat(ResourceUtils.getFile(resourceLocation).exists()).isTrue();

		resourceLocation = "classpath:skip/products_no_error.txt";
		assertThat(ResourceUtils.isUrl(resourceLocation)).isTrue();
		assertThat(ResourceUtils.getFile(resourceLocation).exists()).isTrue();
	}

	@Test(expected = Exception.class)
	public void absoluteClasspathResource() throws Exception {

		// NOTE: 절대 경로를 사용하면 파일을 찾을 수 없습니다.
		//
		String resourceLocation = "classpath:/skip/products_no_error.txt";
		assertThat(ResourceUtils.isUrl(resourceLocation)).isTrue();
		assertThat(ResourceUtils.getFile(resourceLocation).exists()).isTrue();
	}

	@Test
	public void fileSystemResource() throws Exception {
		String resourceLocation = "file:./products.txt";

		log.debug("Current Folder=[{}]", Files.currentFolder());


		URL url = ResourceUtils.getURL(resourceLocation);
		assertThat(ResourceUtils.isFileURL(url)).isTrue();
		assertThat(ResourceUtils.getFile(resourceLocation).exists()).isTrue();

		resourceLocation = "file:products.txt";
		url = ResourceUtils.getURL(resourceLocation);
		assertThat(ResourceUtils.isFileURL(url)).isTrue();
		assertThat(ResourceUtils.getFile(resourceLocation).exists()).isTrue();
	}
}
