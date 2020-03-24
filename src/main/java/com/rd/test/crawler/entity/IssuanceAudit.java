package com.rd.test.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Honyelchak
 * @create 2020-03-23 17:16
 * 发行审核
 */
@Data
@AllArgsConstructor
@ToString
public class IssuanceAudit implements Serializable {
    private Long id;

    private String memberOfCommittee;
    private String auditedIssuer;

    private OpenDirectory openDirectory;
}
