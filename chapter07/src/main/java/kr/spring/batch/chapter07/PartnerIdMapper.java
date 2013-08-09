package kr.spring.batch.chapter07;

import kr.spring.batch.chapter07.jpa.PartnerMappingRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * kr.spring.batch.chapter07.PartnerIdMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오전 10:11
 */
@Slf4j
public class PartnerIdMapper {

    @Setter
    private String partnerId;

    @Setter
    private PartnerMappingRepository partnerMappingRepository;


    public Product map(Product partnerProduct) {
        assert partnerMappingRepository != null;
        assert partnerProduct != null;

        log.info("retrieve partnerId=[{}], productId=[{}]", partnerId, partnerProduct.getId());
        PartnerMapping mapping = partnerMappingRepository.findByPartnerIdAndProductId(partnerId, partnerProduct.getId());

        partnerProduct.setId(mapping.getId());
        return partnerProduct;
    }
}
