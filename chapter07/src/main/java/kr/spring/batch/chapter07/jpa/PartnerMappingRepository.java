package kr.spring.batch.chapter07.jpa;

import kr.spring.batch.chapter07.PartnerMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * kr.spring.batch.chapter07.jpa.PartnerMappingRepository
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 2:59
 */
public interface PartnerMappingRepository extends JpaRepository<PartnerMapping, String> {

    @Query("select pm from PartnerMapping pm where pm.partnerId = :partnerId and pm.productId = :productId")
    PartnerMapping findByPartnerIdAndProductId(@Param("partnerId") String partnerId, @Param("productId") String productId);
}
