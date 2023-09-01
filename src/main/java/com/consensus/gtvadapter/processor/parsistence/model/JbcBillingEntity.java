package com.consensus.gtvadapter.processor.parsistence.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "JCBILLINGENTITY", schema = "J2CORE")
public class JbcBillingEntity {

    @Id
    @Column(name = "jcentitykey")
    private Long jcEntityKey;

    @Column(name = "orgid")
    private Long orgId;
}
