package com.ssk.hfb.utils;

import com.ssk.hfb.pojo.Sign;
import com.ssk.hfb.pojo.WinXinEntity;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Map;

@Slf4j
public class WXUnitl {
    // 获取token
    private static String getAccessToken() {
        String access_token = "";
        String grant_type = "";//获取access_token填写client_credential
        String AppId="";//第三方用户唯一凭证
        String secret="";//第三方用户唯一凭证密钥，即appsecret
        //这个url链接地址和参数皆不能变  这个token是需要进行缓存的，因为它的调用次数每日是有限的。

        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+AppId+"&secret="+secret;  //访问链接

        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes);
            log.info("access_token:"+message);
            JSONObject demoJson = JSONObject.fromObject(message);
            log.info("access_token:"+demoJson);

            access_token = demoJson.getString("access_token");

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("access_token:"+access_token);
        return access_token;
    }

    public static String getSignature(String jsapi_ticket, String nonce_str, String timestamp, String url) {
        // 注意这里参数名必须全部小写，且必须有序
        String string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url="
                + url;

        String signature = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
    public static WinXinEntity getWinXinEntity(String url) {
        WinXinEntity wx = new WinXinEntity();
        String access_token = getAccessToken();  //同类的  上面的获取token的方法
        String ticket = getTicket(access_token);  //同类的  上面的获取ticket的方法
        Map<String, String> ret = Sign.sign(ticket, url);
        wx.setTicket(ret.get("jsapi_ticket"));
        wx.setSignature(ret.get("signature"));
        wx.setNoncestr(ret.get("nonceStr"));
        wx.setTimestamp(ret.get("timestamp"));
        return wx;
    }



    // 获取ticket
    private static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + access_token + "&type=jsapi";// 这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.fromObject(message);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ticket:"+ticket);
        return ticket;
    }


}

