package com.lwc.api;

import com.lwc.api.support.UserSupport;
import com.lwc.domain.JsonResponse;
import com.lwc.domain.UserMoment;
import com.lwc.domain.annotation.ApiLimitedRole;
import com.lwc.domain.annotation.DataLimited;
import com.lwc.domain.constant.AuthRoleConstant;
import com.lwc.service.UserMomentService;
import com.lwc.service.UserMomentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: UserMomentsApi
 * Description:
 *
 * @Author 林伟朝
 * @Create 2024/10/14 10:12
 */
@RestController
public class UserMomentApi {
    @Autowired
    private UserMomentService userMomentService;

    @Autowired
    private UserSupport userSupport;



    //获取用户订阅的up主动动态信息
    @GetMapping("user-subscribed-moments")
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments() {
        Long userId = userSupport.getCurrentUserId();
        List<UserMoment>list=userMomentService.getUserSubscribedMoments(userId);
        return new JsonResponse<>(list);
    }


}
