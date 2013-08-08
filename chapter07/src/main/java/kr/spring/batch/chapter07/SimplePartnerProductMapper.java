package kr.spring.batch.chapter07;

/**
 * kr.spring.batch.chapter07.SimplePartnerProductMapper
 *
 * @author 배성혁 sunghyouk.bae@gmail.com
 * @since 13. 8. 8. 오후 3:07
 */
public class SimplePartnerProductMapper implements PartnerProductMapper {
    @Override
    public Product map(PartnerProduct partnerProduct) {
        return new Product(partnerProduct.getId(),
                           partnerProduct.getTitle(),
                           partnerProduct.getDetails(),
                           partnerProduct.getPrice());
    }
}
