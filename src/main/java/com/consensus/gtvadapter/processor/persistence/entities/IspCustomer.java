package com.consensus.gtvadapter.processor.persistence.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "ISPCUSTOMER", schema = "ISPPOWER")
public class IspCustomer {

    @Id
    @Column(name = "customerkey")
    private Long customerKey;

    @Column(name = "country")
    private String country;

    @Column(name = "oem_id")
    private Long oemId;

    @Column(name = "company_code")
    private String companyCode;
}
