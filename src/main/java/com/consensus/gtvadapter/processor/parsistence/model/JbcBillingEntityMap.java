package com.consensus.gtvadapter.processor.parsistence.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Data
@Entity
@Table(name = "JCBILLINGENTITY_MAP", schema = "J2CORE")
public class JbcBillingEntityMap {

    @Id
    @Column(name = "oem_id")
    private Long oemId;

    @Column(name = "countryisocode")
    private String countryIsoCode;

    @Column(name = "todate")
    private Date toDate;

    @Column(name = "billingentitykey")
    private Long billingEntityKey;

    @Column(name = "company_code")
    private String companyCode;

}
