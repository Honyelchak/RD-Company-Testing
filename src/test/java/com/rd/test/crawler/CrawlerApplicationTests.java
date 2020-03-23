package com.rd.test.crawler;

import com.rd.test.crawler.entity.IssuanceAudit;
import com.rd.test.crawler.entity.OpenDirectory;
import com.rd.test.crawler.mapper.IssuanceAuditMapper;
import com.rd.test.crawler.mapper.OpenDirectoryMapper;
import org.apache.ibatis.builder.annotation.MapperAnnotationBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CrawlerApplicationTests {

    private OpenDirectoryMapper openDirectoryMapper;
    private IssuanceAuditMapper issuanceAuditMapper;

    @Test
    public void contextLoads() {
    }

    @Test
    public void insert(){

        OpenDirectory openDirectory = new OpenDirectory(1L, "xxxxxxxxxxxxxxx", "1", "vyftyfyt", "xxxx", "ttu", "hiuiu", new Date(), null);
        List<IssuanceAudit> list = new ArrayList<>();
        IssuanceAudit issuanceAudit = null;
        for (long i = 1; i < 5; i++) {
            issuanceAudit = new IssuanceAudit(i, "xxx", "xxxxxx", openDirectory);
            list.add(issuanceAudit);
        }

        openDirectory.setIssuanceAuditList(list);
        issuanceAuditMapper.insertMany(list);
        openDirectoryMapper.insert(openDirectory);
    }

}
