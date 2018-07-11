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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Auther: zhao quan
 * @Date: 2018/7/9 09:34
 * @PACKAGE_NAME: com.websocket.web.config
 * @Description:
 */
@Component
@ServerEndpoint("/webSocket/{roomName}/{username}")
public class WebSocketAddRoom {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //在线人数
    public int onlineNumber;

    //以用户的姓名为key，WebSocket为对象保存起来
    private static Map<String, Map<String, WebSocketAddRoom>> clients = new ConcurrentHashMap<>();

    //会话
    private Session session;

    //用户名
    private String username;

    //房间名
    private String roomName;

    //发送消息的时间


    /**
     * 功能描述: 建立连接
     *
     * @Param:
     * @Return:
     * @Auther: zhao quan
     * @Date: 2018/7/9 10:22
     */
    @OnOpen
    public void onOpen(@PathParam("roomName") String roomName,
                       @PathParam("username") String username, Session session) {
        this.session = session;
        this.roomName = roomName;
        this.username = username;
        Map<String, Object> map1 = new HashMap<>();
        map1.put("messageType", 1);
        map1.put("username", username);
        //判断房间名是否已经存在
        if (!clients.containsKey(roomName)) {
            Map<String, WebSocketAddRoom> map = new HashMap<>();
            map.put(username, this);
            logger.info("创建了名为房间号：" + roomName);
            logger.info("现在连接进来的客户id: " + session.getId() + "用户名: " + username + "房间号为" + roomName);
            this.onlineNumber = 1;
            try {
                sendMessageToAll(JSON.toJSONString(map1), roomName);
                clients.put(roomName, map);
                logger.info(toString());
            } catch (IOException e) {
                logger.info(username + "上线的时候通知所有人发生了错误");
            }
        } else {
            logger.info("现在连接进来的客户id: " + session.getId() + "用户名: " + username + "房间号为" + roomName);
            Map<String, WebSocketAddRoom> map3 = clients.get(roomName);
            clients.put(username,map3);
            try {
                sendMessageToAll(JSON.toJSONString(map1), roomName);
                map3.put(username, this);
                logger.info(toString());
            } catch (IOException e) {
                logger.info(username + "上线的时候通知所有人发生了错误");
            }
        }
        //在线人员
        Set<String> set = clients.get(roomName).keySet();
        Map<String, Object> map2 = new HashMap<>();
        map2.put("messageType", 3);
        map2.put("onlineUsers", set);
        try {
            sendMessageTo(JSON.toJSONString(map2), roomName, username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.info("服务端发生了错误" + error.getMessage());
        //error.printStackTrace();
    }

    @OnClose
    public void onClose(@PathParam("roomName") String roomName, @PathParam("username") String username) {
        if (clients.containsKey(roomName)) {
            onlineNumber--;
            clients.get(roomName).remove(username);
            Map<String, WebSocketAddRoom> map = clients.get(roomName);
            List<String> users = new ArrayList<>();
            for (String user : map.keySet()) {
                users.add(user);
            }
            Map<String, Object> map1 = new HashMap<>();
            map1.put("messageType", 2);
            map1.put("onlineUsers", users);
            map1.put("offlineUser",username);
            try {
                sendMessageToAll(JSON.toJSONString(map1), roomName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("房间号为：" + roomName + "有连接关闭！ 当前在线人数" + onlineNumber);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        JSONObject jsonObject = JSON.parseObject(message);
        String textMessage = jsonObject.getString("message");
        String roomName = jsonObject.getString("roomName");
        String fromUsername = jsonObject.getString("username");
        String toUsername = jsonObject.getString("to");
        logger.info("来自房间号为：" + roomName + "客户端消息：" + message + "  客户端ID为：" + session.getId());
        //如果不是发给所有，那么就发给某一个人
        for (String room : clients.keySet()) {
            if (room.equals(roomName)) {
                Map<String, Object> map1 = new HashMap<>();
                map1.put("messageType", 4);
                map1.put("textMessage", textMessage);
                map1.put("fromUsername", fromUsername);
                map1.put("toUserName", toUsername);
                try {
                    if (toUsername.equals("All")) {
                        map1.put("toUsername", "所有人");
                        sendMessageToAll(JSON.toJSONString(map1), roomName);
                    } else {
                        map1.put("toUsername", toUsername);
                        sendMessageTo(JSON.toJSONString(map1), roomName, toUsername);
                    }
                } catch (IOException e) {
                    logger.info("发生了错误");
                }
            }
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
    private void sendMessageTo(String message, String roomName, String toUsername) throws IOException {
        flag:for (String room : clients.keySet()) {
            //判断是否在同一房间
            if (room.equals(roomName)) {
                for (WebSocketAddRoom item : clients.get(roomName).values()) {
                    //判断发送人
                    if (item.username.equals(toUsername)) {
                        item.session.getAsyncRemote().sendText(message);
                        break flag;
                    }
                }
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
    public void sendMessageToAll(String message, String roomName) throws IOException {
        for (String room : clients.keySet()) {
            //判断是否在同一房间
            if (room.equals(roomName)) {
                System.out.println("------------------------------------");
                for (WebSocketAddRoom item : clients.get(roomName).values()) {
                    item.session.getAsyncRemote().sendText(message);
                }
            }
        }
    }

    public synchronized int getOnlineCount() {
        return onlineNumber;
    }

    @Override
    public String toString() {
        return "WebSocketAddRoom{" +
                "onlineNumber=" + onlineNumber +
                ", session=" + session +
                ", username='" + username + '\'' +
                ", roomName='" + roomName + '\'' +
                '}';
    }
}