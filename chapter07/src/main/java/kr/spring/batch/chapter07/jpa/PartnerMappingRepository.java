package kr.spring.batch.chapter07.jpa;

import kr.spring.batch.chapter07.PartnerMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * kr.spring.batch.chapter07.jpa.PartnerMappingRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:59
 */
public interface PartnerMappingRepository extends JpaRepository<PartnerMapping, String> {

    PartnerMapping findByPartnerIdAndProductId(String partnerId, String productId);
}
