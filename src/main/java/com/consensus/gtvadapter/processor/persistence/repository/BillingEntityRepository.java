package com.consensus.gtvadapter.processor.persistence.repository;

import com.consensus.gtvadapter.processor.persistence.model.JbcBillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillingEntityRepository extends JpaRepository<JbcBillingEntity, Long> {

    @Query("SELECT be FROM IspCustomer cust INNER JOIN JbcBillingEntityMap m ON m.countryIsoCode = cust.country AND  m.oemId = cust.oemId AND ((m.companyCode = cust.companyCode) OR (m.companyCode is null AND cust.companyCode is null )) INNER JOIN JbcBillingEntity be ON be.jcEntityKey = m.billingEntityKey WHERE cust.customerKey = :customerKey AND m.toDate > CURRENT_DATE")
    JbcBillingEntity findBillingEntityByCustomerKey (@Param("customerKey") Long customerKey);
}
