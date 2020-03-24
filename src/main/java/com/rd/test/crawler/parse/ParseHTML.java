package com.rd.test.crawler.parse;

import com.rd.test.crawler.entity.IssuanceAudit;
import com.rd.test.crawler.entity.OpenDirectory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * @author Honyelchak
 * @create 2020-03-24 10:51
 * 解析请求返回的HTML文件
 */
public class ParseHTML {

    /**
     * 解析中国证券监督管理委员会公开目录
     *
     * @return
     */
    public static OpenDirectory parseOpenDirectory(String html) {

        String index = Optional.ofNullable(regExpMatcher(html, RegexString.INDEX_REGEX, 1)).orElse("无");
        String category = Optional.ofNullable(regExpMatcher(html, RegexString.CATEGORY_REGEX, 1)).orElse("无");
        String issuingAgency = Optional.ofNullable(regExpMatcher(html, RegexString.ISSUING_AGENCY_REGEX, 1)).orElse("无");
        String meetingName = Optional.ofNullable(regExpMatcher(html, RegexString.MEETING_NAME_REGEX, 1)).orElse("无");
        String symbol = Optional.ofNullable(regExpMatcher(html, RegexString.SYMBOL_REGEX, 1)).orElse("无");
        String keywords = Optional.ofNullable(regExpMatcher(html, RegexString.KEYWORDS_REGEX, 1)).orElse("无");
        String publicationDateStr = Optional.ofNullable(regExpMatcher(html, RegexString.PUBLICATION_DATE_REGEX, 1)).orElse("无");
        Date publicationDate = strToDate(publicationDateStr);

        OpenDirectory openDirectory = new OpenDirectory(null, meetingName, index, issuingAgency, category, symbol, keywords, publicationDate, null);

        return openDirectory;
    }

    /**
     * 解析IssuanceAudit
     *
     * @return
     */
    public static List<IssuanceAudit> parseIssuanceAudit(String src) {
        List<IssuanceAudit> list = new ArrayList<>();

        String memberOfCommittee = null;
        String auditedIssuer = null;
        Matcher matcher = Pattern.compile(RegexString.ISSUANCE_AUDIT_REGEX).matcher(src);
        // 会匹配多组
        while (matcher.find()) {
            // 处理人名之间的空格、span、p标签
            memberOfCommittee = matcher.group(1).replaceAll(RegexString.CLEAR_HTML_SPAN_REGEX, "").replaceAll(RegexString.CLEAR_HTML_P_REGEX, " ").trim();
            memberOfCommittee = memberOfCommittee.replaceAll(RegexString.SPACE_REGEX, ",");
            auditedIssuer = matcher.group(2);
            list.add(new IssuanceAudit(null, memberOfCommittee, auditedIssuer, null));
        }
        return list;
    }

    /**
     * 将字符换YYYY年MM月DD日转为DATE对象
     * @param src 源字符串
     * @return
     */
    private static Date strToDate(String src) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY年MM月DD日");
        Date parseDate = null;
        try {
            parseDate = sdf.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parseDate;
    }

    /**
     * 根据传过来的正则表达书和字符串，进行匹配。
     * @param src 源字符串
     * @param regx 正则表达式
     * @param groupIndex group下标
     * @return
     */
    private static String regExpMatcher(String src, String regx, int groupIndex) {
        Matcher matcher = Pattern.compile(regx).matcher(src);
        if (matcher.find()) {
            return matcher.group(groupIndex);
        }
        return null;
    }

    /**
     * GZIP解压字符串
     * 解决Content-Encoding: gzip 的问题
     * @param str 源字符串
     * @return
     * @throws IOException
     */
    public static String uncompressString(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str
                .getBytes("ISO-8859-1"));
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        return out.toString();
    }
}
