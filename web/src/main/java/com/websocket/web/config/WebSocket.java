package com.websocket.web.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Auther: zhao quan
 * @Date: 2018/7/9 09:34
 * @PACKAGE_NAME: com.websocket.web.config
 * @Description:
 */
@Component
@ServerEndpoint("/webSocket/{username}")
public class WebSocket {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //在线人数
    public static int onlineNumber = 0;

    //以用户的姓名为key，WebSocket为对象保存起来
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<>();

    //会话
    private Session session;

    //用户名
    private String username;

    /**
     * 功能描述: 建立连接
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/9 10:22
     */
    @OnOpen
    public void onOpen(@PathParam("username") String username, Session session) {
        onlineNumber++;
        logger.info("现在连接进来的客户id: " + session.getId() + "用户名: " + username);
        this.username = username;
        this.session = session;
        logger.info("用新连接加入，目前在线人数为：" + onlineNumber);
        //messageType 1代表上线 2代表下线 3代表在线名单 4代表普通消息
        //先给所有人发送通知，说我上线了
        Map<String, Object> map1 = new HashMap<>();
        map1.put("messageType", 1);
        map1.put("username", username);
        try {
            sendMessageToAll(JSON.toJSONString(map1), username);
            //把自己的信息加入到map当中去
            clients.put(username, this);
            //给自己发一条消息：告诉自己现在都有谁在线
            Map<String, Object> map2 = new HashMap<>();
            map2.put("messageType", 3);
            //移除掉自己
            Set<String> set = clients.keySet();
            map2.put("onlineUsers", set);
            sendMessageTo(JSON.toJSONString(map2), username);
        } catch (IOException e) {
            logger.info(username + "上线的时候通知所有人发生了错误");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    @OnClose
    public void onClose() {
        onlineNumber--;
        clients.remove(username);
        try {
            //messageType 1代表上线 2代表下线 3代表在线名单  4代表普通消息
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 2);
            map1.put("onlineUsers", clients.keySet());
            map1.put("username", username);
            sendMessageToAll(JSON.toJSONString(map1), username);
        } catch (IOException e) {
            logger.info(username + "下线的时候通知所有人发生了错误");
        }
        logger.info("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端消息：" + message + "  客户端ID为：" + session.getId());
        JSONObject jsonObject = JSON.parseObject(message);
        String textMessage = jsonObject.getString("message");
        String fromUsername = jsonObject.getString("username");
        String toUsername = jsonObject.getString("to");
        //如果不是发给所有，那么就发给某一个人
        Map<String, Object> map1 = new HashMap<>();
        map1.put("messageType", 4);
        map1.put("textMessage", textMessage);
        map1.put("fromUsername", fromUsername);
        try {
            if (toUsername.equals("All")) {
                map1.put("toUsername", "所有人");
                sendMessageToAll(JSON.toJSONString(map1), fromUsername);
            } else {
                map1.put("toUsername", toUsername);
                sendMessageTo(JSON.toJSONString(map1), toUsername);
            }
        } catch (IOException e) {
            logger.info("发生了错误");
        }

    }

    /**
     * 功能描述:私聊
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/9 10:24
     */
    private void sendMessageTo(String message, String ToUserName) {
        for (WebSocket item : clients.values()) {
            if (item.username.equals(ToUserName)) {
                item.session.getAsyncRemote().sendText(message);
                break;
            }
        }
    }

    /**
     * 功能描述:群聊
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/9 10:22
     */
    public void sendMessageToAll(String message, String FromUserName) throws IOException {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineNumber;
    }
}