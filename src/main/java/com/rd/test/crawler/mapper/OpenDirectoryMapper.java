package com.rd.test.crawler.mapper;

import com.rd.test.crawler.entity.OpenDirectory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Honyelchak
 * @create 2020-03-23 17:38
 */
@Mapper
public interface OpenDirectoryMapper {

    @Insert("INSERT INTO `open_directory` (`index`, `category`, `issuing_agency`, `meeting_name`, `symbol`, `keywords`, `publication_date`) VALUES (#{index}, #{category}, #{issuing_agency},#{meeting_name}, #{symbol}, #{keywords}, #{publication_date})")
    public int insert(OpenDirectory openDirectory);

}
