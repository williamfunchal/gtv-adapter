package com.consensus.gtvadapter.processor.persistence.repository;

import com.consensus.gtvadapter.processor.persistence.model.J2CorpProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CorpProfileRepository extends JpaRepository<J2CorpProfile, String> {

    @Query(value = MIN_COMMITMENT_SUBSCRIPTION_QUERY, nativeQuery = true)
    Float findMinCommitmentSubscription(@Param("resellerId") String resellerId);

    J2CorpProfile findByResellerId(String resellerId);

    final String MIN_COMMITMENT_SUBSCRIPTION_QUERY = "select (select distinct jpi.currency_amount " +
            " from isppower.offer_code_rules ocr, ISPPOWER.JFAX_PRICE_ITEM jpi " +
            " where ocr.offer_code = prof.offercode " +
            " and OCR.PRICE_CODE = jpi.price_code " +
            " and jpi.price_item_type in ('M') " +
            " union " +
            " select distinct jpi1.unit_amount * (select jpi.currency_amount from isppower.jfax_price_item jpi, isppower.deal_code_details dcd, isppower.offer_code_rules ocr " +
            "                                    where ocr.offer_code = ocr1.offer_code and ocr.deal_code = dcd.deal_code " +
            "                                    and dcd.resource_type = 'INBOX_GENERIC' " +
            "                                    and dcd.price_code = jpi.price_code " +
            "                                    and jpi.price_item_type ='N' " +
            "                                    ) " +
            " from isppower.offer_code_rules ocr1, ISPPOWER.JFAX_PRICE_ITEM jpi1 " +
            " where ocr1.offer_code = prof.offercode " +
            " and OCR1.PRICE_CODE = jpi1.price_code " +
            " and jpi1.price_item_type in ('T')) " +
            " from isppower.j2_corp_profile prof " +
            " where prof.resellerid = :resellerId";
}
