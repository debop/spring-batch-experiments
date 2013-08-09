package kr.spring.batch.chapter08.test.beans;

/**
 * kr.spring.batch.chapter08.test.beans.BusinessService
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 9. 오후 2:19
 */
public interface BusinessService {

    String reading();

    void writing(String item);

    void processing(String item);
}
