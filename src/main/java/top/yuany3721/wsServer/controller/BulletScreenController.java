package top.yuany3721.wsServer.controller;

import top.yuany3721.wsServer.socket.WebSocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yuany3721.wsServer.util.BulletBuffer;

import javax.websocket.Session;
import java.util.Deque;
import java.util.Map;

@Controller
@RequestMapping("/")
@SuppressWarnings("unused")
public class BulletScreenController {

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(){
        return "hello";
    }

    @ResponseBody
    @RequestMapping(value = "/newBullet", method = RequestMethod.GET)
    public void newBullet(String message){
        BulletBuffer.getInstance().newBullet(message);
    }

}