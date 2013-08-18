package kr.spring.batch.chapter14.test.batch.samples;

import org.fest.assertions.Assertions;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * kr.spring.batch.chapter14.test.batch.samples.MockitoSampleTest
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 18. 오후 9:39
 */
public class MockitoSampleTest {

	@SuppressWarnings("unchecked")
	@Test
	public void mockInterface() {
		List<String> mockedList = mock(List.class);
		mockedList.add("one");
		mockedList.clear();

		verify(mockedList, times(1)).add("one");
		verify(mockedList, times(1)).clear();
		verifyNoMoreInteractions(mockedList);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void mockConcreteClass() {
		LinkedList<String> mockedList = mock(LinkedList.class);
		when(mockedList.get(0)).thenReturn("first");

		Assertions.assertThat(mockedList.get(0)).isEqualTo("first");
	}

	@Test
	public void spyTest() {
		List<String> list = new LinkedList<String>();
		List<String> spy = spy(list);
		spy.add("one");
		spy.add("two");

		verify(spy).add("one");
		verify(spy).add("two");
	}
}
