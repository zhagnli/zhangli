package com.it;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class News {

    public static void main(String[] args) throws javax.mail.MessagingException, MessagingException{
        String url = "http://zhouxunwang.cn/data/";
        String key = "UeHErYEzTYv+h5yO9o03RmzDPQTgsJeZ/px16A";
        String type[] = {"top", "shehui", "guonei", "guoji", "yule"
                , "tiyu", "junshi", "keji", "caijing", "shishang"};
        String parms = "id=75&key=" + key + "&type=" + type[0];
        String str = UrlGet(url, parms);
        String content = JsonSpilt(str);

        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.yuanian.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress("zhagnli01@yuanian.com"));
        // 设置收件人邮箱地址
        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("wangyq@yuanian.com"),new InternetAddress("qiugch@yuanian.com"),new InternetAddress("wangmh@yuanian.com")});
        //message.setRecipient(Message.RecipientType.TO, new InternetAddress("zhaowei@yuanian.com"));//一个收件人
        // 设置邮件标题
        message.setSubject("培训-新闻资讯  张丽  "+new Date());
        // 设置邮件内容
//        message.setText(Content);//纯文本发送
        //HTML格式标签发送

        String event1 = "新闻资讯：";
        message.setContent(event1+"<table border=\\\"5\\\" style=\\\"border:solid 1px #E8F2F9;font-size=14px;;font-size:18px;>" +
                "<tr style=\\\"background-color: #428BCA; color:#ffffff\\\">" +
                "<th>新闻类别</th><th>时间</th><th>标题</th><th>作者</th><th>访问链接</th></tr>" +
                content +
                "</table>","text/html; charset=utf-8");
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("zhangli01@yuanian.com", "13260676137zl");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();

    }

    public static String UrlGet(String url, String parms) {
        String Content = "";
        BufferedReader read = null;
        try {
            URL realurl = new URL(url + "?" + parms);
            URLConnection connection = realurl.openConnection();
            connection.connect();
            read = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = read.readLine()) != null) {
                Content += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Content;
    }
    public static String JsonSpilt(String str) {
        Map<Object, Object> result = jsonToMap(str);
        Map<Object, Object> date = jsonToMap(result.get("result"));
        JSONArray AD= (JSONArray)date.get("data");
        StringBuilder stbu = new StringBuilder();
        for (int i = 0; i < AD.size(); i++) {
            JSONObject JD = AD.getJSONObject(i);
            String title = JD.getString("title");
            String time = JD.getString("date");
            String category = JD.getString("category");
            String author_name = JD.getString("author_name");
            String url= JD.getString("url");

            String text = "<tr>"+"<td>"+category+"新闻"+"</td>"
                    +"<td>"+time+"</td>"
                    +"<td>"+title+"</td>"
                    +"<td>"+author_name+"</td>"
                    +"<td>"+url+"</td>"
                    +"</tr>";
            stbu.append(text);
        }

        return stbu.toString();
    }
    public static Map<Object, Object> jsonToMap(Object jsonObj) {
        JSONObject jsonObject = JSONObject.fromObject(jsonObj);
        Map<Object, Object> map = (Map)jsonObject;
        return map;
    }

}
