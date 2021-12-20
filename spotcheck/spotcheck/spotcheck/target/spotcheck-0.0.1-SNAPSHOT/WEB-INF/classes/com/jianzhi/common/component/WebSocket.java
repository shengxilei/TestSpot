package com.jianzhi.common.component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/websocket/{userName}")
//此注解相当于设置访问URL
public class WebSocket {
    
    private Session session;
    
    private static CopyOnWriteArraySet<WebSocket> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
    
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userName")String userName) {
        this.session = session;
        webSockets.add(this);
        sessionPool.put(userName, session);
        System.out.println(userName+"【消息】有新的连接，总数为:"+webSockets.size());
    }
    
    @OnClose
    public void onClose() {
        webSockets.remove(this);
        System.out.println("【消息】连接断开，总数为:"+webSockets.size());
    }
    
    @OnMessage
    public void onMessage(String message) {
        System.out.println("【消息】收到客户端消息:"+message);
    }
    
    // 此为广播消息
    public void sendAllMessage(String message) {
        for(WebSocket webSocket : webSockets) {
            System.out.println("【消息】广播消息:"+message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // 此为单点消息
    public void sendOneMessage(String userName, String message) {
    	 System.out.println("【消息】单点消息:"+message);
        Session session = sessionPool.get(userName);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}