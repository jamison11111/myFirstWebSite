package com.lwc.api.support;

import com.lwc.domain.RefreshTokenDetail;
import com.lwc.domain.exception.ConditionException;
import com.lwc.service.UserService;
import com.lwc.service.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * ClassName: UserSupport
 * Description:
 *控制器类的相关支持类的包内的类,这是User控制器的支持类
 * @Author 林伟朝
 * @Create 2024/10/10 16:24
 */
@Component
public class UserSupport {

    @Autowired
    private UserService userService;

    //本方法用于解析前端请求头中的令牌,双令牌校验
    public Long getCurrentUserId() {
        //从前端请求头中获取token令牌
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String accesstoken=requestAttributes.getRequest().getHeader("accesstoken");
        //先校验,后查询,校验不通过的话,才查数据库,提升系统性能
        Long userId= TokenUtil.verifyAccessToken(accesstoken);
        if(userId==null||userId<0){
            throw new ConditionException("accessToken,前端收到此异常后，请代替用户向本服务端调用刷新token接口" +
                    "尝试更新accessToken,若更新成功，再用accessToken来登录!");
        }
    return userId;
    }
}
