package com.example.beans;

import com.example.utils.SendWX;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class WxBean {
    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public WXBizMsgCrypt wxMsgCrypt() {
        try {
            return new WXBizMsgCrypt(environment.getProperty("wx.token"), environment.getProperty("wx.aeskey"),
                    environment.getProperty("wx.corpid"));
        } catch (AesException e) {
            e.printStackTrace();
        }
        return null;
    }
}
