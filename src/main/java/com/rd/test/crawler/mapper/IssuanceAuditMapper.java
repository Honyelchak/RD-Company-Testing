package com.rd.test.crawler.mapper;

import com.rd.test.crawler.entity.IssuanceAudit;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * @author Honyelchak
 * @create 2020-03-23 17:38
 */
@Mapper
public interface IssuanceAuditMapper {

    @Insert({"<script>",
            "INSERT INTO `issuance_audit` ",
            "(`member_of_committee`, `audited_issuer`, `working_meeting_id`) VALUES ",
            "<foreach collection=\"list\" index=\"index\" separator=\",\" item=\"item\">",
                "(#{memberOfCommittee},#{auditedIssuer},#{openDirectory.id});",
            "</foreach>",
            "</script>"
    })
    public int insertMany(List<IssuanceAudit> issuanceAudits);
}
