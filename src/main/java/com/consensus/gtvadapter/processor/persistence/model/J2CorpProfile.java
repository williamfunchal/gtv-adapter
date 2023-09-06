package com.consensus.gtvadapter.processor.persistence.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "J2_CORP_PROFILE", schema = "ISPPOWER")
public class J2CorpProfile {

    @Id
    @Column(name = "RESELLERID")
    private String resellerId;

    @Column(name = "PAYMENT_TERMS")
    private String paymentTerms;

    @Column(name = "OFFERCODE")
    private String offerCode;


}
