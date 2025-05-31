package com.lwc.service.websocket;

import com.alibaba.fastjson.JSONObject;
import com.lwc.domain.Danmu;
import com.lwc.domain.constant.DanmuConstant;
import com.lwc.service.DanmuService;
import com.lwc.service.util.TokenUtil;
import io.netty.util.internal.StringUtil;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: WebSocketService
 * Description:
 * WebSocket服务类,也是一个bean
 *
 * @Author 林伟朝
 * @Create 2024/10/26 14:35
 */
/*通过"/socketServerEndPoint"来请求服务端的websocket端点*/

//前端会在适当的时候通过javascipt脚本向本后台服务器发送形如ws://localhost:8080/socketServerEndPoint/{token}
//的websocket请求来建立与后台服务器的websocket长连接,并在适当的时候发起断开连接请求断开websocket长连接
//这里何时发起连接和何时断开的逻辑细节前端程序员会帮我们处理好,后端程序员只需把websocket连接端点和接口暴露给前端即可。
@Component
@ServerEndpoint("/socketServerEndPoint/{token}")
public class WebSocketService {
    //本类的相关日志对象
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //AtomicInger是维持了原子性操作的Integer封装类,使用它的原因是在高并发场景下需要保持在线人数这个属性的线程安全性
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    //这个Map也是为了线程安全而被制造出来的,每个客户端都有一个WebSocketService bean对象(多例模式),通过这个Map来查找
    public static final ConcurrentHashMap<String, WebSocketService> WEBSOCKET_MAP = new ConcurrentHashMap<>();

    //每个客户端与服务端之间建立通信后都有一个对话,也就是这个session属性
    private Session session;

    //每个WebSocketService bean的唯一标识字段
    private String sessionId;

    private Long userId;

    private static ApplicationContext APPLICATION_CONTEXT;

    public WebSocketService() {
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    //当客户端和服务端成功建立一次WebSocket长连接对象后，会进入对应的WebSocektService bean对象并调用这个建立成功的方法
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        //从请求路径的路径参数中获取令牌信息，解析出用户id(若令牌令牌存在且合法，也就是客户端处于登录状态，才解析的出来)
        try {
            this.userId = TokenUtil.verifyAccessToken(token);
        } catch (Exception e) {}
        //为此连接的bean对象相应的字段值赋值
        sessionId = session.getId();
        this.session = session;
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            /*说明本用户之前已经与服务端建立过一次长连接了(而且还没下线)但不知何故,这里又建立了一次长连接，(
            比如同时开两个浏览器窗口看同一个视频(但是是同一个登录用户，只能算一个在线人数,这只是我的猜想）
            所以删掉Map里的旧对象换成新的，并且在线人数不变,会话维持两个还是一个有待验证，我觉得是两个。
             */
            WEBSOCKET_MAP.remove(sessionId);
            WEBSOCKET_MAP.put(sessionId, this);
        } else//这次会话是一次新会话连接，之前没见过，在线人数需要加一
        {
            WEBSOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户:" + sessionId + "连接成功,当前在线人数为:" + ONLINE_COUNT.get());
        //还需告知前端连接成功了,没办法,websocket这个把消息给前端的方法会抛出受检异常（非RunTime Exception），只能try,catch一下
        try {
            this.sendMessage("0");
        } catch (IOException e) {
            logger.error("连接异常");
        }

    }

    //服务端发送字符串消息给前端的接口
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    //客户端或服务端请求关闭此连接时，进入此接口做善后工作
    @OnClose
    public void closeConnection() {
        if (WEBSOCKET_MAP.containsKey(sessionId)) {
            WEBSOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户:" + sessionId + "退出,当前在线人数为:" + ONLINE_COUNT.get());


    }



    //本接口定时向连接的客户端返回当前视频在线观看的人数
    @Scheduled(fixedRate = 5000)//5s钟触发一次的定时任务
    private void noticeOnlineCount() throws IOException {
        //遍历获取所有处于连接状态的客户端，向他们发消息即可
        for(Map.Entry<String,WebSocketService>entry:WebSocketService.WEBSOCKET_MAP.entrySet()){
            WebSocketService webSocketService = entry.getValue();
            if(webSocketService.session.isOpen()){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("onlineCount",ONLINE_COUNT.get());
                jsonObject.put("msg","当前在线人数为:+"+ONLINE_COUNT.get());
                webSocketService.sendMessage(jsonObject.toJSONString());
            }
        }
    }

    //本bean的相关方法抛出异常时,进入此接口
    @OnError
    public void oneError(Throwable throwable) {

    }

    public Session getSession() {
        return session;
    }
}
