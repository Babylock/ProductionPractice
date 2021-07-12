package com.example.utils;

import org.springframework.core.env.Environment;

public class SendWX {
    static public void send(String username, String content, Environment environment){
        WeChatMsgSend swx = new WeChatMsgSend();
        try{
            String token = swx.getToken(environment.getProperty("wx.corpid"),environment.getProperty("wx.corpsecret"));
            String postdata = swx.createpostdata(username, "text", 1000002, "content", content);
            String resp = swx.post("utf-8", WeChatMsgSend.CONTENT_TYPE, (new WeChatUrlData()).getSendMessage_Url(), postdata, token);
            System.out.println("发送微信的响应数据======>" + resp);
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
