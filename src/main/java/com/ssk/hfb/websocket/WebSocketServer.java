package com.ssk.hfb.websocket;

import com.ssk.hfb.common.utils.JsonUtils;
import com.ssk.hfb.pojo.Message;
import com.ssk.hfb.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author zhengkai.blog.csdn.net
 */
@ServerEndpoint("/msg/{userId}")
@Component
@Slf4j
public class WebSocketServer {
    @Autowired
    private MessageService messageService;
    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。*/
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId="";
    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            webSocketMap.put(userId,this);
            log.info("用户连接已存在:"+userId+",当前在线人数为:" + getOnlineCount());
            //加入set中
        }else{
            webSocketMap.put(userId,this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("用户:"+userId+",网络异常!!!!!!");
        }
    }
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息*/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:"+userId+",报文:"+message);
        //可以群发消息
        //消息保存到数据库、redis
        if(StringUtils.isNotBlank(message)){
            try {
                //解析发送的报文
                Map<String, String> map = JsonUtils.toMap(message, String.class, String.class);
                if (!CollectionUtils.isEmpty(map)){
                    String toUserId=map.get("fid");
                    String msg = map.get("data");
                    //传送给对应toUserId用户的websocket
                    if(StringUtils.isNotBlank(toUserId)&&webSocketMap.containsKey(toUserId)){
                        int fromId = Integer.valueOf(userId);
                        int toId = Integer.valueOf(toUserId);
//                        messageService.saveMsg(fromId, toId);
                        HashMap<String, String> m = new HashMap<>();
                        m.put("fromId", userId);
                        m.put("toId", toUserId);
                        m.put("data", message);
                        String serialize = JsonUtils.serialize(m);
                        webSocketMap.get(toUserId).sendMessage(serialize);
                    }else{
                        log.error("请求的userId:"+toUserId+"不在该服务器上");
                        //否则不在这个服务器上，发送到mysql或者redis
                    }
                }
//                JSONObject jsonObject = JSON.parseObject(message);
//                //追加发送人(防止串改)
//                jsonObject.put("fromUserId",this.userId);

            }catch (Exception e){
               log.error("错误信息: "+e.getMessage());
               e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }
    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    /**
     * 发送自定义消息
     * */
    public static void sendInfo(Message message) throws IOException {
        String toId =String.valueOf(message.getToId());
        log.info("发送消息到:"+toId+"，报文:"+message.getMessage());
        log.info("列表:"+webSocketMap);

        if(StringUtils.isNotBlank(toId)&&webSocketMap.containsKey(toId)){
            String data = JsonUtils.serialize(message);
            webSocketMap.get(toId).sendMessage(data);
        }else{
            log.error("用户"+message.getToId()+",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }
}
