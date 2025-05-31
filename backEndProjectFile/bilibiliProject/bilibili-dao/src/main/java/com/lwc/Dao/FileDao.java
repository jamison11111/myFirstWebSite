package com.lwc.Dao;

import com.lwc.domain.File;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ClassName: FileDao
 * Description:
 *
 * @Author 林伟朝
 * @Create 2024/10/22 9:50
 */
@Mapper
public interface FileDao {
    //将第一次上传的文件的md5字符流信息等上传到数据库
    Integer addFile(File file);
    //根据md5字符流去数据库查询有无此份文件的记录
    File getFileByMD5(String md5);


    List<File> getAllFile();

    void delelteFilesByPaths(List<String> paths);
}


