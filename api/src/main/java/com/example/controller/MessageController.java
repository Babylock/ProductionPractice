package com.example.controller;

import com.example.utils.SendWX;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;


@RestController
@RequestMapping("/weixin")
public class MessageController {

    private WXBizMsgCrypt wxBizMsgCrypt;

    @Autowired
    public void setWxBizMsgCrypt(WXBizMsgCrypt wxBizMsgCrypt) {
        this.wxBizMsgCrypt = wxBizMsgCrypt;
    }

    @RequestMapping(value = {"/", ""}, method = RequestMethod.GET)
    @ResponseBody
    public String validation(@RequestParam(name = "msg_signature") String msgSignature, @RequestParam(name = "timestamp") String timestamp,
                             @RequestParam(name = "nonce") String nonce, @RequestParam(name = "echostr") String echostr)
    {
        try {
            return wxBizMsgCrypt.VerifyURL(msgSignature, timestamp, nonce, echostr);
        } catch (AesException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public String handleMessage(@RequestParam(name = "msg_signature") String msgSignature,
                                @RequestParam(name = "timestamp") String timestamp,
                                @RequestParam(name = "nonce") String nonce,
                                @RequestBody String reqData) {
        try {
            String msg = wxBizMsgCrypt.DecryptMsg(msgSignature, timestamp, nonce, reqData);
            System.out.println(msg);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            StringReader sr = new StringReader(msg);
            InputSource is = new InputSource(sr);
            Document document = null;
            try {
                document = db.parse(is);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("Content");
            String content = nodelist1.item(0).getTextContent();
            System.out.println("Content???" + content);
            nodelist1 = root.getElementsByTagName("FromUserName");
            String toUserName = nodelist1.item(0).getTextContent();
            System.out.println("UserName: " + toUserName);
            String buffer = null;
            StringBuilder answer = new StringBuilder();
            // ????????????
            BufferedReader bufferReader = null;
            try {
                //???????????????????????????????????????Python???????????????????????????
                Process process = Runtime.getRuntime().exec(
                        String.format("python3 %s %s", environment.getProperty("python.path"), content)
                );
                //??????Python???????????????
                bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                while ((buffer = bufferReader.readLine()) != null) {
                    answer.append(buffer);
                }
                //???????????????????????????????????????
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // ????????????
            //SendWX.send(toUserName, content, environment);
            SendWX.send(toUserName, answer.toString(), environment);

        } catch (AesException e) {
            e.printStackTrace();
        }
        return "";
    }

    @GetMapping("/test")
    public String test() {
        BufferedReader bufferReader = null;
        try {
            //???????????????????????????????????????Python???????????????????????????
            Process process = Runtime.getRuntime().exec("python C:\\Users\\bblk\\Desktop\\main.py");
            System.out.println("xss");
            //??????Python???????????????
            bufferReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    private Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
