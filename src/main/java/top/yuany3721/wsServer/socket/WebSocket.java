package top.yuany3721.wsServer.socket;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
@ServerEndpoint(value = "/connect")
@Component
public class WebSocket {

    // 当前在线的客户端
    public static final Map<String, Session> clients = new ConcurrentHashMap<>();
    // 当前在线连接数
    public static final AtomicInteger onlineCount = new AtomicInteger(0);
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS  ");

    @OnOpen
    public void onOpen(Session session) {
        onlineCount.incrementAndGet();
        clients.put(session.getId(), session);
        System.out.println(format.format(new Date()) + "new connect established：" + session + "\n当前连接数：" + onlineCount.get());
        try {
            session.getBasicRemote().sendText(session.toString().split("@")[1] + "+-+-+成功连接弹幕服务器，这是一条测试弹幕");
        } catch (Exception e) {
            System.err.println("连接确认消息发送失败");
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1
        clients.remove(session.getId());
        System.out.println(format.format(new Date()) + session + " closed\n剩余连接数：" + onlineCount.get());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println(format.format(new Date()) + "received from(" + session+ "):" + message);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.err.println(format.format(new Date()) + "WebSocket Error");
        error.printStackTrace();
    }

    /**
     * 广播消息
     *
     * @param message 消息内容
     */
    public void sendMessage(String message) {
        for (Map.Entry<String, Session> sessionEntry : clients.entrySet()) {
            Session toSession = sessionEntry.getValue();
            toSession.getAsyncRemote().sendText(message);
        }
    }

}