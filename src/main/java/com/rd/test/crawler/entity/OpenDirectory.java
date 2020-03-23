package com.rd.test.crawler.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Honyelchak
 * @create 2020-03-23 17:09
 * 开放目录
 */
@Data
@AllArgsConstructor
public class OpenDirectory implements Serializable {
    private Long id;
    private String meetingName;
    private String index;
    private String issuingAgency;
    private String category;
    private String symbol;
    private String keywords;
    private Date publicationDate;

    private List<IssuanceAudit> issuanceAuditList;
}
