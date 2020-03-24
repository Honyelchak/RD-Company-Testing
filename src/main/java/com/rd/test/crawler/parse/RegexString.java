package com.rd.test.crawler.parse;

/**
 * @author Honyelchak
 * @create 2020-03-24 10:55
 * 存储一些正则表达式字符串常量
 */
public class RegexString {


    public final static String INDEX_REGEX = "<B>索 引 号:</B>(.*)</td>";
    public final static String CATEGORY_REGEX = "<span id=\"lSubcat\">(.*)</span>";
    public final static String ISSUING_AGENCY_REGEX = "<B>发布机构:</B>[\\s]*<span>(.*)</span>";
    public final static String MEETING_NAME_REGEX = "<span id=\"lTitle\">(.*)</span>";
    public final static String SYMBOL_REGEX = "<B>文　　号:</B>[\\s]*<span>(.*)</span>";
    public final static String KEYWORDS_REGEX= "<B>主 题 词:</B>[\\s]*<span>(.*)</span>";
    public final static String PUBLICATION_DATE_REGEX = "<B>发文日期:</B>[\\s]*<span>(.*)</span>";

    public final static String ISSUANCE_AUDIT_REGEX = "参会发审委委员</SPAN></P>[\\r|\\n|\\r\\n?]<P>[^\\w]*<SPAN>([\\s\\S]*?)</SPAN></P>[\\r|\\n|\\r\\n?]<P>[\\S]*<SPAN>审核的发行人：</SPAN></P>[\\r|\\n|\\r\\n?]<P>[\\S]*<SPAN>(.*)</SPAN>";
    public final static String CLEAR_HTML_SPAN_REGEX = "</SPAN><SPAN>[\\u3000|\\u0020|\\u00A0]*";
    public final static String CLEAR_HTML_P_REGEX = "</SPAN></P>(\\r|\\n|\\r\\n)*<P>[\\u3000|\\u0020|\\u00A0]*<SPAN>";
    public final static String SPACE_REGEX = "[\\u3000|\\u0020|\\u00A0]+";
}
