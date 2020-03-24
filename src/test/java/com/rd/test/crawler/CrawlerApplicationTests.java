package com.rd.test.crawler;

import com.rd.test.crawler.entity.IssuanceAudit;
import com.rd.test.crawler.entity.OpenDirectory;
import com.rd.test.crawler.mapper.IssuanceAuditMapper;
import com.rd.test.crawler.mapper.OpenDirectoryMapper;
import com.rd.test.crawler.parse.ParseHTML;
import com.rd.test.crawler.request.Request;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.sound.midi.Soundbank;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerApplicationTests {

    @Resource
    private OpenDirectoryMapper openDirectoryMapper;
    @Resource
    private IssuanceAuditMapper issuanceAuditMapper;
    // 请求头Headers
    private static Map<String, String> map = null;
    // 请求链接
    private static String url = "http://www.csrc.gov.cn/pub/zjhpublic/G00306202/201712/t20171229_330048.htm";

    @Test
    public void contextLoads() {
    }


    public void init() {
        map = new HashMap<>();
        map.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        map.put("Accept-Encoding", "gzip, deflate");
        map.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        map.put("Cache-Control", "max-age=0");
        map.put("Connection", "keep-alive");
        map.put("Host", "www.csrc.gov.cn");
        map.put("Upgrade-Insecure-Requests", "1");
        map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.120 Safari/537.36");
    }

    @Test
    public void crawelTest() {
        // 初始化headers
        init();
        // 根据请求ulr和请求头请求链接文本。
        String result = Request.sendRequest(url, map);
        // 解析html文本(IssuanceAudit)
        List<IssuanceAudit> issuanceAudits = ParseHTML.parseIssuanceAudit(result);
        // 解析html文本(OpenDirectory)
        OpenDirectory openDirectory = ParseHTML.parseOpenDirectory(result);
        // 打印
        System.out.println(openDirectory);
        issuanceAudits.forEach(System.out::println);
        // 关联对象
        int successSum = openDirectoryMapper.insert(openDirectory);
        if (successSum < 1) {
            System.out.println("插入失败！");
        }
        for (IssuanceAudit issuanceAudit:issuanceAudits) {
            issuanceAudit.setOpenDirectory(openDirectory);
        }

        issuanceAuditMapper.insertMany(issuanceAudits);


    }
}
