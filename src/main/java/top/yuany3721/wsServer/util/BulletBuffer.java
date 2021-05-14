package top.yuany3721.wsServer.util;

import top.yuany3721.wsServer.socket.WebSocket;

import javax.websocket.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BulletBuffer {
    private static final BulletBuffer instance = new BulletBuffer();
    private static final Deque<String> buffer = new ArrayDeque<>();
    private static final AtomicInteger totalBullets = new AtomicInteger(0);
    // 最大弹幕数量
    private static final int MAX_BULLET_BUFFER_NUM = 150;
    // 弹幕最小推送间隔（ms）
    private static final int PUSH_SCHEDULE = 2;

    public static BulletBuffer getInstance() {
        return instance;
    }

    private BulletBuffer(){
        pushBullet();
    }

    /**
     * 新建弹幕
     * @param message 弹幕内容
     */
    public void newBullet(String message){
        if (totalBullets.get() >= MAX_BULLET_BUFFER_NUM){
            // 缓存超限则将最早进入队列的弹幕丢弃
            buffer.removeLast();
            totalBullets.decrementAndGet();
        }
        // 新弹幕入队
        buffer.addFirst(message);
        totalBullets.incrementAndGet();
    }

    /**
     * 获取要推送的弹幕
     * @return bullet String
     */
    public String getBullet(){
        if (totalBullets.get() > 0){
            totalBullets.decrementAndGet();
            return buffer.removeFirst();
        }
        return "";
    }

    /**
     * 推送弹幕
     */
    private void pushBullet(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                String bullet = getBullet();
                if (bullet.length() == 0)
                    return;
                if (WebSocket.onlineCount.get() == 0){
                    newBullet(bullet);
                    return;
                }
                System.out.println(111);
                broadcastBullet(bullet);
            }
        }, 0, PUSH_SCHEDULE);
    }

    private void broadcastBullet(String bullet){
        // 广播消息
        for (Map.Entry<String, Session> sessionEntry : WebSocket.clients.entrySet()) {
            Session toSession = sessionEntry.getValue();
            toSession.getAsyncRemote().sendText(bullet);
        }
    }
}
