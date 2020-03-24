package com.rd.test.crawler.mapper;

import com.rd.test.crawler.entity.OpenDirectory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

/**
 * @author Honyelchak
 * @create 2020-03-23 17:38
 */
@Mapper
public interface OpenDirectoryMapper {

    @Insert("INSERT INTO `open_directory` (`index`, `category`, `issuing_agency`, `meeting_name`, `symbol`, `keywords`, `publication_date`) VALUES (#{index}, #{category}, #{issuingAgency},#{meetingName}, #{symbol}, #{keywords}, #{publicationDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public int insert(OpenDirectory openDirectory);

}
