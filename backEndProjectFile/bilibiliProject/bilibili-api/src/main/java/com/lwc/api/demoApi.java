package com.lwc.api;

import com.lwc.domain.JsonResponse;
import com.lwc.service.demoService;
import com.lwc.service.util.FastDFSUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * ClassName: demoAPI
 * Description:
 *
 * @Author 林伟朝
 * @Create 2024/10/8 22:05
 */
@RestController
public class demoApi {
    @Autowired
    private demoService service;
    @Autowired
    private FastDFSUtil fastDFSUtil;
    //操作搜索引擎服务器的相关api
//    @Autowired
//    private ElasticSearchService elasticSearchService;
    //用于调用另一个微服务的依赖bean的注入


    @GetMapping("/testing")
    public JsonResponse<String> testOn(){
        return new JsonResponse<>("恭喜你,ping通了!");
    }

    @GetMapping("/queryTest")
    public String queryTest(Integer age){
        return service.query(age);
    }


    //文件分片的测试接口,临时文件会先存放在客户端本机，或存放在前端服务器
    @PostMapping("/slices")
    public void slices(MultipartFile file)throws Exception{
        fastDFSUtil.convertFileToSlices(file);
    }








}
