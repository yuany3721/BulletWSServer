package top.yuany3721.wsServer.controller;

import org.springframework.web.bind.annotation.RequestParam;
import top.yuany3721.wsServer.socket.WebSocket;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import top.yuany3721.wsServer.util.BulletBuffer;

import javax.websocket.Session;
import java.util.Deque;
import java.util.Map;
import java.util.Random;

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

    @ResponseBody
    @RequestMapping(value = "/testBullet", method = RequestMethod.GET)
    public void testBullet(@RequestParam("message") String message,@RequestParam("count") String count){
        for (int i = 0; i < Integer.parseInt(count); i++){
            BulletBuffer.getInstance().newBullet(message.substring(new Random().nextInt(message.length())));
        }
    }

}