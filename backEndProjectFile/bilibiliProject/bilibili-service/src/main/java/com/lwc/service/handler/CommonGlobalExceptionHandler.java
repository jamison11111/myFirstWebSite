package com.lwc.service.handler;

import com.lwc.domain.JsonResponse;
import com.lwc.domain.exception.ConditionException;
import com.sun.org.apache.bcel.internal.generic.RETURN;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Condition;

/**
 * ClassName: CommonGlobalExceptionHandler
 * Description:
 * 这是一个全局异常处理类，本项目抛出的无人解决的异常最终由此类handle，这个类的优先级最高(在处理异常方面)
 * 业务层模块的工作包括了异常处理,故写在此模块
 *
 * @Author 林伟朝
 * @Create 2024/10/9 20:31
 */


@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {
    //下面这两个注解是修饰本类的异常处理方法的
    @ExceptionHandler(value = Exception.class)//接收所有类型的未处理异常
    @ResponseBody//本类的此API若被调用，则其返回值(若存在)会作为响应体返回给前端
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        //此API的两个形参，前者是调用此顶级异常处理器的请求本身，后者是其所除法的异常
        String errorMsg = e.getMessage();
        //如果是条件异常类，则该类会有特制的异常信息和状态码(在项目中可以自定义传入定制化的条件异常类状态码,告诉前端发生了某种特定类型的异常(比如密码错误))
        if (e instanceof ConditionException) {
            String errCode = ((ConditionException)e).getCode();
            return new JsonResponse<>(errCode,errorMsg);
        }
        //如果不是的这种条件异常类的话，就给一个统一的异常状态码"500"
        else {
            return new JsonResponse<>("500",errorMsg);
        }

    }
}
